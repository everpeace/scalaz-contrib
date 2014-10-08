package scalaz.contrib.rxscala

import org.junit.runner.RunWith
import org.scalacheck.Prop._
import org.specs2.runner.JUnitRunner
import org.specs2.scalaz.Spec
import rx.lang.scala.Observable

import scalaz._
import Scalaz._
import scalaz.scalacheck.ScalazProperties._

@RunWith(classOf[JUnitRunner])
class ObservableSpec extends Spec {
  import scalaz.contrib.rxscala.ImplicitsForTest._

  checkAll(equal.laws[Observable[Int]])
  checkAll(monoid.laws[Observable[Int]])
  checkAll(monad.laws[Observable])
  checkAll(monadPlus.strongLaws[Observable])
  checkAll(isEmpty.laws[Observable])
  checkAll(traverse.laws[Observable])

  forAll { (ob: Observable[Int], f: Int => Int) =>
    (ob <*|*> (_ map f)) === (ob zip (ob map f))
  }
}
