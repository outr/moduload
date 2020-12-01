package spec

import moduload.{Moduload, Priority}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.{ExecutionContext, Future}

class ModuloadSpec extends AsyncWordSpec with Matchers {
  "Moduload" should {
    "load three modules" in {
      Moduload.load().map { _ =>
        Module1.loaded should be(true)
        Module2.loaded should be(true)
        Module3.loaded should be(true)
        ModuloadSpec.order.reverse should be(List("Module2", "Module1", "Module3"))
      }
    }
  }
}

object ModuloadSpec {
  var order = List.empty[String]
}

object Module1 extends Moduload {
  var loaded: Boolean = false

  override def load()(implicit ec: ExecutionContext): Future[Unit] = {
    loaded = true
    ModuloadSpec.order = "Module1" :: ModuloadSpec.order
    println("Module 1 loaded!")
    Future.successful(())
  }

  override def error(t: Throwable): Unit = t.printStackTrace()
}

object Module2 extends Moduload {
  var loaded: Boolean = false

  override def load()(implicit ec: ExecutionContext): Future[Unit] = {
    loaded = true
    ModuloadSpec.order = "Module2" :: ModuloadSpec.order
    println("Module 2 loaded!")
    Future.successful(())
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.High
}

object Module3 extends Moduload {
  var loaded: Boolean = false

  override def load()(implicit ec: ExecutionContext): Future[Unit] = {
    loaded = true
    ModuloadSpec.order = "Module3" :: ModuloadSpec.order
    println("Module 3 loaded!")
    Future.successful(())
  }

  override def error(t: Throwable): Unit = t.printStackTrace()

  override def priority: Priority = Priority.Low
}