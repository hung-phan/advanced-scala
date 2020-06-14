package com.hung_phan

object SelfTypes extends App {
  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    this: Instrumentalist => // SELF-TYPE: whoever implements Singer to implement Instrumentalist

    // rest of the implementation or API
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  // ILLEGAL
//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T =>
  } // S REQUIRES a T

  // CAKE PATTERN => "Dependency injection"
  // enforce the type to be injected

  // classical DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependantComponent(val component: Component)

  // In Scala we do cake pattern
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependantComponent { this: ScalaComponent =>
    def dependantAction(x: Int): String = s"${action(x)} this rocks!"
  }
  trait ScalaApplication { this: ScalaDependantComponent =>
  }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose component
  trait Profile extends ScalaDependantComponent with Picture
  trait Analytics extends ScalaDependantComponent with Picture

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics

  // cyclical dependencies
//  class X extends Y
//  class Y extends X

  trait X { self: Y =>}
  trait Y { self: X =>}
}
