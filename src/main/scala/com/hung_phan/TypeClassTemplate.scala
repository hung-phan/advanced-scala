package com.hung_phan

object TypeClassTemplate {
  // Summary
  // Type class definition example
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](
      implicit instance: MyTypeClassTemplate[T]
    ): MyTypeClassTemplate[T] = instance
  }
}
