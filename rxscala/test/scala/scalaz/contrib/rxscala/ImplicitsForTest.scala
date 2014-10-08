package scalaz.contrib.rxscala

import org.scalacheck.Arbitrary
import rx.lang.scala.Observable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}
import scala.language.higherKinds
import scalaz._
import scalaz.scalacheck.ScalaCheckBinding._

/**
 * This object provides implicits for tests only.
 */
object ImplicitsForTest {

  // Equality based on generated List (THIS IS FOR TEST ONLY.)
  implicit def observableEqual[A](implicit eqA: Equal[A]) = new Equal[Observable[A]] {
    def equal(a1: Observable[A], a2: Observable[A]) = a1.toBlocking.toList == a2.toBlocking.toList
  }

  implicit def observableShow[A](implicit showA: Show[A], showLA: Show[List[A]]) = Show.shows[Observable[A]](ob =>
    "Observable" + Show.showContravariant.contramap[List[A], Observable[A]](showLA)(_.toBlocking.toList).show(ob))

  implicit def observableArbitrary[A](implicit a: Arbitrary[A], array: Arbitrary[Array[A]]): Arbitrary[Observable[A]] = Functor[Arbitrary].map(array)(Observable.just(_: _*))

}
