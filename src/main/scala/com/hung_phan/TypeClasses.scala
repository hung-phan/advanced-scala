package com.hung_phan

object TypeClasses extends App {
  // bad design
//  trait HTMLWritable {
//    def toHtml: String
//  }
//
//  case class User(name: String, age: Int, email: String) extends HTMLWritable {
//    override def toHtml: String =
//      s"<div>$name ($age yo) <a href=$email /></div>"
//  }
//
//  println(User("John", 32, "john@rockthejvm.com").toHtml)

  case class User(name: String, age: Int, email: String)

  // better design
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email} /></div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")

  println(UserSerializer.serialize(john))

  // best design
  object HTMLSerializer {
    def serializer[T](
      value: T
    )(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] =
      serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String =
      s"<div style='color: blue;'>$value</div>"
  }

  println(HTMLSerializer.serializer(42))
  // or
  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
  }

  // Cool!
  println(john.toHTML) // println(new HTMLEnrichment[User](john).toHTML(UserSerializer))
  println(2.toHTML)


  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  // context bounds
  // this will bound HTMLSerializer with type T
  def htmlSugar[T: HTMLSerializer](content: T): String =
    s"<html><body>${content.toHTML}</body></html>"

  // implicitly
  case class Permission(mask: String)
  implicit val defaultPermission: Permission = Permission("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permission]

  def htmlSugarImplicitly[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]

    s"<html><body>${content.toHTML(serializer)}</body></html>"
  }
}
