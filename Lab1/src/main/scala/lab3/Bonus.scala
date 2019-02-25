package lab3

object Bonus extends App{

  // Bonus 1

  sealed trait IntList {
    def map(f: Int => Int): IntList = this match {
      case End => End
      case Node(head, tail) => Node(f(head), tail.map(f))
    }
  }
  case object End extends IntList
  case class Node(head: Int, tail: IntList) extends IntList


  val intList = Node(1, Node(2, Node(3, Node(4, End))))

  assert(intList.map(x => x * 3) == Node(1 * 3, Node(2 * 3, Node(3 * 3, Node(4 * 3, End)))))
  assert(intList.map(x => 5 - x) == Node(5 - 1, Node(5 - 2, Node(5 - 3, Node(5 - 4, End)))))


  // Bonus 2

  sealed trait GenericList[A] {

    def length: Int = this match {
      case GenericEnd() => 0
      case GenericNode(head, tail) => tail.length + 1
    }

    // applies function `f` to every member of GenericList
    def map[B](f: A => B): GenericList[B] = this match {
      case GenericEnd() => GenericEnd()
      case GenericNode(head, tail) => GenericNode(f(head), tail.map[B](f))
    }
  }

  case class GenericEnd[A]() extends GenericList[A]
  case class GenericNode[A](head: A, tail: GenericList[A]) extends GenericList[A]

  val genericList: GenericList[Int] = GenericNode(1, GenericNode(2, GenericNode(3, GenericEnd())))
  assert(genericList.map(x => x + 8) == GenericNode(1 + 8, GenericNode(2 + 8, GenericNode(3 + 8, GenericEnd()))))
  assert(genericList.map(x => x.toString) == GenericNode("1", GenericNode("2", GenericNode("3", GenericEnd()))))
}
