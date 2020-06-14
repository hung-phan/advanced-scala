package com.hung_phan

object Reflection extends App {
  // reflection + macros + quasiquotes => META Programming
  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  // step 0 - import
  import scala.reflect.runtime.{universe => ru}

  // step 1 - mirror
  val m = ru.runtimeMirror(getClass.getClassLoader)

  // step 2 - create a class object
  // this is a description
  val clazz = m.staticClass("com.hung_phan.Reflection.Person") // creating a class object by NAME

  // step 3 - create a reflected mirror
  // this is the real class and can do thing like construct or invoke method
  val cm = m.reflectClass(clazz)

  // step 4 - get the constructor
  val constructor = clazz.primaryConstructor.asMethod

  // step 5 - reflect the constructor
  val constructorMirror = cm.reflectConstructor(constructor)

  // step 6 - invoke the constructor
  val instance = constructorMirror.apply("John")

  println(instance)

  // I have an instance
  val p = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName"
  // 1 - mirror
  // 2 - reflect the instance
  val reflected = m.reflect(p)
  // 3 - method symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method
  val method = reflected.reflectMethod(methodSymbol)

  method.apply()

  // type erasure

  // pp #1: differentiate types at runtime
  val numbers = List(1, 2, 3)
  numbers match {
    case listOfString: List[String] => println("list of strings")
    case listOfString: List[Int] => println("list of numbers")
  }

  // pp #2: limitations on overloads
//  def processList(list: List[Int]): Int = 43
//  def processList(list: List[String]): Int = 45

  // TypeTags

  // 0 - import
  import ru._

  // 1 - creating a type tag "manually"
  val ttag = typeTag[Person]
  println(ttag.tpe)

  class MyMap[K, V]

  // 2 - pass tags as implicit parameters
  def getTypeArgument[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArgument(myMap) // (typeTag: TypeTag[MyMap[Int, String])
  println(typeArgs)

  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal

  println(isSubtype[Dog, Animal])

  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method
  val anotherMethod = reflected.reflectMethod(anotherMethodSymbol)

  anotherMethod.apply()
}
