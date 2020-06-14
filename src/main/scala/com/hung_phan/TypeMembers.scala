package com.hung_phan

object TypeMembers extends App {
  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
//  val dog: ac.AnimalType = ???
//  val cat: ac.BoundedAnimal = new Cat
  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int

    override def add(element: T): MyList = ???
//    override def add(element: Int): MyList = ???
  }

  type CatsType = cat.type
  // still compilable
  val newCat: CatsType = cat

  /**
   * Exercise - enforce a type to be applicable to SOME TYPES only
   */
  trait MList {
    type A
    def head: A
    def tail: MList
  }
  trait ApplicableToNumbers {
    type A <: AnyVal
  }

  // NOT OK
  class CustomList(hd: Int, tl: CustomList) extends MList with ApplicableToNumbers {
    override type A = Int

    override def head = hd

    override def tail = tl
  }
}
