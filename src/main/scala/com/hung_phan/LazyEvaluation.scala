package com.hung_phan

object LazyEvaluation extends App {
  def byNameMethod(n: => Int): Int = {
    lazy val t = n
    t + t + t + 1
  }

  def retrieveMagicValue(): Int = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))
}
