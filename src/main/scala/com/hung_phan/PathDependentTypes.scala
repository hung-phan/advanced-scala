package com.hung_phan

object PathDependentTypes extends App {
  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
  val otherInner: oo.Inner = new oo.Inner

  o.print(inner)
  // NOT OK
  //  oo.print(inner)

  // path-dependant types

  // Outer#Inner
  o.printGeneral(inner)
  o.printGeneral(otherInner)

  /**
   * Exercise
   * DB keyed by Int or String, but maybe others
   */

  /**
   * use path-dependant types
   * abstract type members and/or type aliases
   */
  trait ItemLike {
    type Key
  }
  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("home") // ok
  //  get[IntItem]("scala") // not ok
}
