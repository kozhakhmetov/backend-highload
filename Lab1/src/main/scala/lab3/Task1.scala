package lab3

sealed trait Shape {
  def sides: Int

  def perimeter: Double

  def Area: Double
}

trait Rectangular extends Shape


case class Circle(radius: Double) extends Shape {
  override def sides: Int = 0

  override def perimeter: Double = 2 * radius * Math.PI

  override def Area: Double = radius * radius * Math.PI
}

case class Rectangle(width: Double, height: Double) extends Rectangular {
  override def sides: Int = 4

  override def perimeter: Double = (height + width) * 2

  override def Area: Double = height * width
}

case class Square(side: Double) extends Rectangular {
  override def sides: Int = 4

  override def perimeter: Double = side * 4

  override def Area: Double = side * side
}

object Draw {
  def apply(shape: Shape): String = shape match {
    case Circle(radius) => s"A circle of radius $radius cm"
    case Rectangle(width, height) => s"A rectangle of width $width cm and height $height cm"
    case Square(side) => s"A circle with a side of $side cm"
  }
}

object Task1 extends App {


  println(Draw(Circle(10)))
  println(Draw(Rectangle(3, 4)))

}
