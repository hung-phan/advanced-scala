package com.hung_phan

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MagnetPattern extends App {
  // to solve the problem of method overloading
  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
  }

  /**
    * 1 - type eraser
    * 2 - lifting doesn't work for all overloads
    *
    * val receive = receive _// ?!
    * 3 - code duplication
    * 4 - type inference and default args
    *
    * actor.receive(?!)
    */
  // with above problem, we can rewrite is as below
  trait MessageMagnet[T] {
    def apply(): T
  }

  def receive[T](magnet: MessageMagnet[T]): T = magnet()

  implicit class FromP2PRequest(request: P2PRequest)
      extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P request")
      42
    }
  }
  implicit class FromP2PResponse(request: P2PResponse)
      extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1 - no more type erasure problem!
  implicit class FromResponseFuture(future: Future[P2PResponse])
      extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }
  implicit class FromRequestFuture(future: Future[P2PRequest])
      extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int) = x + 1
    def add1(x: String) = x.toInt + 1
  }

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }
  implicit class AddString(x: String) extends AddMagnet {
    override def apply(): Int = x.toInt + 1
  }
  val addFV: AddMagnet => Int = add1 _

  println(addFV(5))
  println(addFV("5"))

  /**
   * Drawbacks
   * 1 - more verbose
   * 2 - harder to read
   * 3 - you can't name of place default arguments
   * 4 - call by name doesn't work correctly
   */
}
