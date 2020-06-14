package com.hung_phan

object StructuralTypes {
  // structural types
  type JavaClosable = java.io.Closeable

  class HipsterClosable {
    def close(): Unit = println("yeah yeah I'm closing")
  }

//  def closeQuietly(closable: JavaClosable OR HipsterClosable) // ?!
  type UnifiedClosable = {
    def close(): Unit
  } // STRUCTURAL TYPE

  def closeQuietly(closable: UnifiedClosable) = closable.close()

  closeQuietly(new JavaClosable {
    override def close(): Unit = ???
  })
  closeQuietly(new HipsterClosable)


  // TYPE REFINEMENTS

  type AdvanceClosable = JavaClosable {
    def closeSilently(): Unit
  }

  class AdvancedJavaClosable extends JavaClosable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advClosable: AdvancedJavaClosable): Unit = advClosable.closeSilently()

  closeShh(new AdvancedJavaClosable)
  // this won't work even the HipsterClosable implements closeSilently
  //  closeShh(new HipsterClosable)

  // using structural types as standalone types
  def altClose(closable: { def close(): Unit }): Unit = closable.close()

  // type-checking => duck typing
  type SoundMaker = {
    def makeSound(): Unit
  }
  class Dog {
    def makeSound(): Unit = println("bark")
  }
  class Car {
    def makeSound(): Unit = println("yroom")
  }
  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // CAVEAT: based on reflection
  // have heavy impact on performance

  /**
   * Exercise
   */
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }
   class Human {
     def head: Brain = new Brain
   }
  class Brain {
    override def toString: String = "BRAINZ!"
  }
  def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)
  /**
   * is compatible with a CBL and with a human
   */
  case object CBNil extends CBL[Nothing] {
    override def head: Nothing = ???
    override def tail: CBL[Nothing] = ???
  }
  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human)

  object HeadEqualizer {
    type Headable[T] = {
      def head: T
    }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }
  /**
   * is compatible with a CBL and with a human
   */
  val brainList = CBCons(new Brain, CBNil)
  val stringList = CBCons("Brainz", CBNil)
  HeadEqualizer.===(brainList, new Human)
  // problem
  HeadEqualizer.===(stringList, new Human) // not type safe

  // it relies on reflection so the type parameter is erased at runtime
  // so be very careful with structural type
}
