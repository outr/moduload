package spec

import moduload.Moduload
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.{ExecutionContext, Future}

class ModuloadSpec extends AsyncWordSpec with Matchers {
  "Moduload" should {
    "load Module1" in {
      Moduload.load().map { _ =>
        Module1.loaded should be(true)
      }
    }
  }
}

object Module1 extends Moduload {
  var loaded: Boolean = false

  override def load()(implicit ec: ExecutionContext): Future[Unit] = {
    loaded = true
    println("Module 1 loaded!")
    Future.successful(())
  }

  override def error(t: Throwable): Unit = t.printStackTrace()
}