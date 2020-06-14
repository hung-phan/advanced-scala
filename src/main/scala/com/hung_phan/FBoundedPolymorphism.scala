package com.hung_phan

object FBoundedPolymorphism extends App {
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat]
//  }

  // Solution 1 - naive
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Cat] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Dog] = ??? // List[Dog]
//  }

  // Solution 2 - F-Bounded polymorphism
//  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = ???
//  }
//
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }

  // Solution 3 - FBP + self-types
//  trait Animal[A <: Animal[A]] { this: A =>
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = ???
//  }

  // Will enforce the type on this
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }

//  trait Fish extends Animal[Fish]
//
//  class Shark extends Fish {
//    // hmm
//    override def breed: List[Animal[Fish]] = List(new Cod) // wrong
//  }
//
//  class Cod extends Fish {
//    override def breed: List[Animal[Fish]] = ???
//  }

  // Exercise
  // Solution 4 - type classes
//  trait Animal
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  object Dog {
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = ???
//    }
//  }
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] =
//      canBreed.breed(animal)
//  }
//  val dog = new Dog
//  dog.breed

  /**
   * new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
   *
   * implicit value to pass to breed: Dog.DogsCanBreed
   */
//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = ???
//    }
//  }
//
//  val cat = new Cat
//  cat.breed
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = ???
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
}
