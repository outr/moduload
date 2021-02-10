package spec

import moduload.{Moduload, Priority}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ModuloadSpec extends AnyWordSpec with Matchers {
  "Moduload" should {
    "load three modules" in {
      Moduload.load()
      Module1.loaded should be(true)
      Module2.loaded should be(true)
      Module3.loaded should be(true)
      ModuloadSpec.order.reverse should be(List("Module2", "Module1", "Module3"))
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