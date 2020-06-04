package com.hung_phan

object PatternMatching extends App {
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  class Person(val name: String, val age: Int)

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty: Boolean = false

      def get: String = person.name
    }
  }

  val bob = new Person("Bob", 25)

  bob match {
    case PersonWrapper(name) => println(s"This person name is $name")
    case _ => println("No match")
  }
}
