package lab3

trait Calculation

case class Success(value: Int) extends Calculation
case class Failure(value: String) extends Calculation

trait Source

case object Well extends Source
case object Spring extends Source
case object Tap extends Source

case class BottledWater(size: Int, source: Source, carbonated: Boolean)

object Task2 extends App{
  // Linked List
  sealed trait IntList {
    def length: Int = {
      def length(intList: IntList, len: Int = 0): Int = intList match {
        case End => len
        case Node (head, tail) => length(tail, len + 1)
      }
      length(this)
    }
    def product: Int = {
      def product(intList: IntList, result: Int = 1): Int = intList match {
        case End => result
        case Node(head, tail) => product(tail, result * head)
      }
      product(this)
    }

    def double2: IntList = {
      def get(intList: IntList = End) : IntList = intList match {
        case End => intList
        case Node(head, tail) => get()
      }
      get(this)
    }
  }
  case object End extends IntList
  case class Node(head: Int, tail: IntList) extends IntList

  val intList = Node(1, Node(2, Node(3, Node(4, End))))

  assert(intList.length == 4)
  assert(intList.tail.length == 3)
  assert(End.length == 0)

  assert(intList.product == 1 * 2 * 3 * 4)
  assert(intList.tail.product == 2 * 3 * 4)
  assert(End.product == 1)

 // assert(intList.double2 == Node(1 * 2, Node(2 * 2, Node(3 * 2, Node(4 * 2, End)))))
 // assert(intList.tail.double2 == Node(4, Node(6, Node(8, End))))
 // assert(End.double2 == End)

  println(intList.double2)

}
