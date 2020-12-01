package moduload

import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.math.Ordering.Implicits._

/**
 * Modules to be loaded should implement this trait
 *
 * See Moduload companion for usage instructions
 */
trait Moduload {
  /**
   * Loads the module. Should not be invoked directly. Calling the companion object's load method should invoke this
   * method exactly once.
   *
   * @param ec the execution context to use for loading
   * @return Future[Unit]
   */
  def load()(implicit ec: ExecutionContext): Future[Unit]

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
  private val loaded = new AtomicBoolean(false)
  private var map = Map.empty[Moduload, Future[Unit]]

  private def load(module: Moduload)(implicit ec: ExecutionContext): Future[Unit] = synchronized {
    map.get(module) match {
      case Some(future) => future
      case None => try {
        val future = module.load().recover {
          case t: Throwable => module.error(t)
        }
        map += module -> future
        future
      } catch {
        case t: Throwable => Future.successful(module.error(t))
      }
    }
  }

  def load()(implicit ec: ExecutionContext): Future[Unit] = if (loaded.compareAndSet(false, true)) {
    val lines = getClass.getClassLoader.getResources("moduload.list").asScala.toList.flatMap { url =>
      val source = Source.fromURL(url)
      try {
        source.getLines().toList.map(_.trim).filter(_.nonEmpty)
      } finally {
        source.close()
      }
    }
    val modules = lines.map { className =>
      val cn = className match {
        case n if n.endsWith("$") => n
        case n => s"$n$$"
      }
      val clazz = Class.forName(cn)
      val field = clazz.getField("MODULE$")
      field.get(None.orNull).asInstanceOf[Moduload]
    }.sortBy(_.priority)
    val futures = modules.map(load)
    Future.sequence(futures).map(_ => ())
  } else {
    Future.successful(())
  }
}