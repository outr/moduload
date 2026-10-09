package spec

import moduload.{Moduload, Priority}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ModuloadSpec extends AnyWordSpec with Matchers {
  "Moduload" should {
    "make a concurrent caller wait for the load in progress" in {
      val first = new Thread(new Runnable {
        override def run(): Unit = Moduload.load()
      })
      first.start()
      val deadline = System.currentTimeMillis() + 5000L
      while (!SlowModule.started && System.currentTimeMillis() < deadline) Thread.sleep(5)
      SlowModule.started should be(true)
      val start = System.currentTimeMillis()
      Moduload.load()
      SlowModule.loaded should be(true)
      (System.currentTimeMillis() - start) should be < 10000L
      first.join()
    }
    "load three modules" in {
      Module1.loaded should be(true)
      Module2.loaded should be(true)
      Module3.loaded should be(true)
      ModuloadSpec.order.reverse should be(List("Module2", "Module1", "Module3"))
    }
    "skip a module whose initialization fails without aborting the others" in {
      SlowModule.loaded should be(true)
    }
    "return at once when a module calls load from the loading thread" in {
      ReentrantModule.loaded should be(true)
    }
  }
}

object ModuloadSpec {
  var order = List.empty[String]
}

object Module1 extends Moduload {
  var loaded: Boolean = false

  override def load(): Unit = {
    loaded = true
    ModuloadSpec.order = "Module1" :: ModuloadSpec.order
    println("Module 1 loaded!")
  }

  override def error(t: Throwable): Unit = t.printStackTrace()
}

object Module2 extends Moduload {
  var loaded: Boolean = false

  override def load(): Unit = {
    loaded = true
    ModuloadSpec.order = "Module2" :: ModuloadSpec.order
    println("Module 2 loaded!")
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.High
}

object Module3 extends Moduload {
  var loaded: Boolean = false

  override def load(): Unit = {
    loaded = true
    ModuloadSpec.order = "Module3" :: ModuloadSpec.order
    println("Module 3 loaded!")
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.Low
}

object BrokenModule extends Moduload {
  if (System.nanoTime() != 0L) throw new IllegalStateException("broken on initialization")

  override def load(): Unit = ()

  override def error(t: Throwable): Unit = t.printStackTrace()
}

object ReentrantModule extends Moduload {
  var loaded: Boolean = false

  override def load(): Unit = {
    Moduload.load()
    loaded = true
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.High
}

object SlowModule extends Moduload {
  @volatile var started: Boolean = false
  @volatile var loaded: Boolean = false

  override def load(): Unit = {
    started = true
    Thread.sleep(500)
    loaded = true
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.Lowest
}
