package moduload

import java.util.concurrent.{CountDownLatch, TimeUnit}
import java.util.concurrent.atomic.AtomicBoolean
import scala.io.Source
import scala.jdk.CollectionConverters._

/**
 * Modules to be loaded should implement this trait
 *
 * See Moduload companion for usage instructions
 */
trait Moduload {
  /**
   * Loads the module. Should not be invoked directly. Calling the companion object's load method should invoke this
   * method exactly once.
   */
  def load(): Unit

  /**
   * Called when an exception is thrown from load.
   *
   * @param t the thrown exception
   */
  def error(t: Throwable): Unit

  /**
   * Used to sort highest priority to lowest
   */
  def priority: Priority = Priority.Normal
}

/**
 * Modules are defined in `moduload.list` files accessible to root of the classloader at runtime. The format for the
 * contents of the files are line separated lists of complete class names (ex. com.company.MyModule) that implements the
 * Moduload trait. At runtime, calling Moduload.load() will load all not previously loaded modules it can find returning
 * a Future that represents all modules loaded state.
 */
object Moduload {
  /**
   * How long a concurrent caller of `load()` waits for the load already in progress on another thread.
   */
  private val ConcurrentLoadWaitMillis: Long = 30000L

  private val loaded = new AtomicBoolean(false)
  private val finished = new CountDownLatch(1)
  @volatile private var loadingThread: Thread = null
  private var set = Set.empty[Moduload]

  private def load(module: Moduload): Unit = synchronized {
    if (!set.contains(module)) {
      try {
        module.load()
        set += module
      } catch {
        case t: Throwable => module.error(t)
      }
    }
  }

  /**
   * Loads every module listed in the classpath's `moduload.list` files, once per JVM. A caller on another thread while
   * the load is in progress waits for it to finish (up to 30 seconds), so a return means every module has been
   * offered. A call from the loading thread itself (a module's load reaching back into Moduload) returns at once.
   */
  def load(): Unit = if (loaded.compareAndSet(false, true)) {
    loadingThread = Thread.currentThread()
    try {
      loadAll()
    } finally {
      loadingThread = null
      finished.countDown()
    }
  } else if (Thread.currentThread() ne loadingThread) {
    if (!finished.await(ConcurrentLoadWaitMillis, TimeUnit.MILLISECONDS)) {
      println(s"WARNING: Moduload load still in progress on another thread after ${ConcurrentLoadWaitMillis}ms; continuing")
    }
  }

  private def loadAll(): Unit = {
    val lines = getClass.getClassLoader.getResources("moduload.list").asScala.toList.flatMap { url =>
      val source = Source.fromURL(url)
      try {
        source.getLines().toList.map(_.trim).filter(_.nonEmpty)
      } finally {
        source.close()
      }
    }
    val modules = lines.flatMap { className =>
      val cn = className match {
        case n if n.endsWith("$") => n
        case n => s"$n$$"
      }
      try {
        val clazz = Class.forName(cn)
        Some(clazz.getField("MODULE$").get(None.orNull).asInstanceOf[Moduload])
      } catch {
        case _: ClassNotFoundException =>
          println(s"WARNING: Moduload unable to find class: $className")
          None
        case t: VirtualMachineError => throw t
        case t: Throwable =>
          println(s"WARNING: Moduload unable to initialize $className: $t")
          None
      }
    }.sortBy(_.priority)
    modules.foreach(load)
  }
}