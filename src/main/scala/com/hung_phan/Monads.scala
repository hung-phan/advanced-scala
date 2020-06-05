package com.hung_phan

object Monads extends App {
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /**
    * Implement a lazy[T] monad = computations which will only be executed if it's needed
    * unit/apply
    * flatMap
    *
    * Monads = unit + flatMap = unit + map + flatten
    *
    * Monad[A] {
    *  def flatMap[B](f: A => Monad[B]): Monad[B] = ???
    *  def map[B](f: A => B): Monad[B] = ???
    *  def flatten(m: Monad[Monad[A]]): Monad[A] = ???
    * }
    */
  class Lazy[+A](newValue: => A) {
    lazy val value = newValue

    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(value)

    def map[B](f: (=> A) => B): Lazy[B] = Lazy(f(value))

    def flatten[B](m: Lazy[Lazy[B]]): Lazy[B] = m.flatMap { (x: Lazy[B]) =>
      x
    }
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }
  val flatMappedInstance = lazyInstance.flatMap { x =>
    Lazy(10 * x)
  }
  val flatMappedInstance2 = lazyInstance.flatMap { x =>
    Lazy(20 * x)
  }

  println(flatMappedInstance.value)
  println(flatMappedInstance2.value)
}
