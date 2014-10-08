package scalaz.contrib.rxscala.examples

import org.junit.runner.RunWith
import org.specs2.matcher.AnyMatchers
import org.specs2.runner.JUnitRunner
import org.specs2.scalaz.{ScalazMatchers, Spec}
import rx.lang.scala.Observable
import rx.lang.scala.Observable.just

import scala.language.higherKinds

/**
 * This demonstrates how you apply Scalaz's operators to Observables.
 */
@RunWith(classOf[JUnitRunner])
class RxScalazDemo extends Spec with AnyMatchers with ScalazMatchers {

  import scalaz.Scalaz._
  import scalaz._
  import scalaz.contrib.rxscala._
  import scalaz.contrib.rxscala.ImplicitsForTest._

  "Observable" should {
    "be applied to Monoid operators" in {
      (just(1, 2) |+| just(3, 4)) must equal(just(1, 2, 3, 4))

      (just(1, 2) ⊹ just(3, 4)) must equal(just(1, 2, 3, 4))

      mzero[Observable[Int]] must equal(Observable.empty)
    }

    "be applied to Functor operators" in {
      (just(1, 2) ∘ { _ + 1 }) must equal(just(2, 3))

      (just(1, 2) >| 5) must equal(just(5, 5))

      (just(1, 2) as 4) must equal(just(4, 4))

      just(1, 2).fpair must equal(just((1, 1), (2, 2)))

      just(1, 2).fproduct { _ + 1 } must equal(just((1, 2), (2, 3)))

      just(1, 2).strengthL("x") must equal(just(("x", 1), ("x", 2)))

      just(1, 2).strengthR("x") must equal(just((1, "x"), (2, "x")))

      Functor[Observable].lift { (_: Int) + 1 }(just(1, 2)) must equal(just(2, 3))
    }

    "be applied to Applicative operators" in {
      1.point[Observable] must equal(just(1))

      1.η[Observable] must equal(just(1))

      (just(1, 2) |@| just(3, 4)) { _ + _ } must equal(just(4, 5, 5, 6))

      (just(1) <*> { (_: Int) + 1 }.η[Observable]) must equal(just(2))

      just(1) <*> { just(2) <*> { (_: Int) + (_: Int) }.curried.η[Observable] } must equal(just(3))

      just(1) <* just(2) must equal(just(1))

      just(1) *> just(2) must equal(just(2))

      Apply[Observable].ap(just(2)) { { (_: Int) + 3 }.η[Observable] } must equal(just(5))

      Apply[Observable].lift2 { (_: Int) * (_: Int) }(just(1, 2), just(3, 4)) must equal(just(3, 4, 6, 8))
    }

    "be applied to Monad and MonadPlus operators" in {
      (just(3) >>= { (i: Int) => just(i + 1) }) must equal(just(4))

      (just(3) >> just(2)) must equal(just(2))

      just(just(1, 2), just(3, 4)).μ must equal(just(1, 2, 3, 4))

      (just(1, 2) <+> just(3, 4)) must equal(just(1, 2, 3, 4))

      PlusEmpty[Observable].empty[Int] must equal(Observable.empty)
    }
    "be applied to Traverse operators" in {
      just(1, 2, 3).foldMap { _.toString } must equal("123")

      just(1, 2, 3).foldLeftM(0)((acc, v) => (acc + v).some) must equal(6.some)

      just(1, 2, 3).suml must equal(6)

      just(1, 2, 3).∀(_ > 0) must equal(true)

      just(1, 2, 3).∃(_ > 2) must equal(true)

      just(1, 2, 3).traverse(x => (x + 1).some) must equal(just(2, 3, 4).some)

      just(1.some, 2.some).sequence must equal(just(1, 2).some)
    }
  }
}
