package com.hung_phan

object ReviewThreadCommunication extends App {
  class SimpleContainer {
    private var value = 0

    def isEmpty(): Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get(): Int = {
      val result = value
      value = 0
      result
    }
  }

  def produceAndConsumeUsingThread(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")

      container.synchronized {
        container.wait()
      }

      println(s"[consumer] I have consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println(s"[producer] I'm producing $value")
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  produceAndConsumeUsingThread()
}
