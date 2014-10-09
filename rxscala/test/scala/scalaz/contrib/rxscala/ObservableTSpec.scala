package scalaz.contrib.rxscala

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.scalaz.Spec

import scalaz._
import Scalaz._
import scalaz.scalacheck.ScalazProperties._

@RunWith(classOf[JUnitRunner])
class ObservableTSpec extends Spec {
  import scalaz.contrib.rxscala._
  import scalaz.contrib.rxscala.ImplicitsForTest._

  type ObservableTOption[A] = ObservableT[Option, A]

  checkAll(equal.laws[ObservableTOption[Int]])

  checkAll(monoid.laws[ObservableTOption[Int]])

  checkAll(monadPlus.laws[ObservableTOption])

}
