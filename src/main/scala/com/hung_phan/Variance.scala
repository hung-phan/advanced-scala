package com.hung_phan

object Variance extends App {
  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics
  class Cage[T]

  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariant
  class ICage[T]
  // this is wrong
  // val icage: ICage[Animal] = new ICage[Cat]
  // equivalent to assign val x: Int = "Hello world"

  // hell no - contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T)

  // covariant positions
  class CovariantCage[+T](val animal: T)

  //  class ContravariantCage[-T](val animal: T)
  // cannot be done
  // if the compiler allows this code
  // val catCage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Crocodile)

  //  class CovariantVariableCage[+T](var animal: T)
  // also cannot be done
  // val cage: CovariantVariableCage[Animal] = new CovariantVariableCage[Cat](new Cat)
  // cage.animal = new Crocodile()

  //  class ContravariantVariableCage[-T](var animal: T)
  // also cannot be done
  // val catCage: ContravariantVariableCage[Cat] = new ContravariantVariableCage[Animal](new Crocodile)

  class InvariantVariableCage[T](var animal: T)

  //  trait AnotherCovariantCage[+T] {
  //    def addAnimal(animal: T) // contravariant position
  //  }
  // if the compiler allows this code
  // val ccage: AnotherCovariantCage[Animal] = new AnotherCovariantCage[Dog]()
  // ccage.add(new Cat)

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)

  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }
  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = emptyList.add(new Cat)
  val evenMoreAnimals = emptyList.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITIONS

  // return type
  class PetShop[-T] {
    // method return types are in covariant position
    //    def get(isAPuppy: Boolean): T
    // if compiler allows this code
    /**
      * val catShop = new PetShop[Animal] {
      *  def get(isAPuppy: Boolean): Animal = new Cat
      * }
      * val dogShop: PetShop[Dog] = catShop
      * dogShop.get(true) // evil cat
      */
    def get[S <: T](isAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  // which will make call like this illegal
//  val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /**
    * Big rule
    * - method arguments are in contravariant position
    * - return types are in covariant position
    */
  /**
    * 1. Invariant, covariant, contravariant
    * Parking[T](things: List[T]) {
    *  park(vehicle: T)
    *  impound(vehicles: List[T])
    *  checkVehicles(conditions: String): List[T]
    * }
    *
    * 2. used someone else's API: IList[T]
    * 3. Parking = monad!
    *  - flatMap
    */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  // invariant
  class ParkingLot1[T <: Vehicle](vehicles: List[T]) {
    def park(vehicle: T): Unit = println(s"Park: $vehicle")
    def impound(vehicles: List[T]): Unit = println(s"Impound: $vehicles")
    def checkVehicles(conditions: String): List[T] = vehicles
  }
  val parkingLot1 = new ParkingLot1[Vehicle](List.empty)

  parkingLot1.park(new Bike)
  parkingLot1.park(new Car)

  // covariant
  class ParkingLot2[+T](vehicles: List[T]) {
    def park[R >: T](vehicle: R): Unit = println(s"Park: $vehicle")
    def impound[R >: T](vehicles: List[R]): Unit = println(s"Impound: $vehicles")
    def checkVehicles(conditions: String): List[T] = vehicles
  }
  val parkingLot2: ParkingLot2[Vehicle] = new ParkingLot2[Car](List.empty)

  parkingLot2.park(new Bike)
  parkingLot2.park(new Car)

  // contravariant
  class ParkingLot3[-T](vehicles: List[T]) {
    def park(vehicle: T): Unit = println(s"Park: $vehicle")
    def impound(vehicles: List[T]): Unit = println(s"Impound: $vehicles")
    def checkVehicles[S <: T](conditions: String): List[S] = ???
  }
  val parkingLot3: ParkingLot3[Car] = new ParkingLot3[Vehicle](List(new Car, new Bike))
//  parkingLot3.park(new Bike)
  parkingLot3.park(new Car)

  /**
   * Rule of thumb
   * - use covariance = Collection of things
   * - use contravariance = Group of actions
   */

  class IList[T]
}
