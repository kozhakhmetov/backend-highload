package lab4

object SeocndPart extends App {

  def divide(a: Int, b: Int): Option[Int] = {
    b match {
      case 0 => None
      case _ => Some(a / b)
    }
  }

  def calculator(operand1: String, operator: String, operand2: String): Option[Int] = {

    def readInt(str: String): Option[Int] =
      if(str matches "\\d+") Some(str.toInt) else None

    val op1: Option[Int] = readInt(operand1)
    val op2: Option[Int] = readInt(operand2)

    def sum(opt1: Option[Int], opt2: Option[Int]): Option[Int] =  {
      for {
        value1 <- opt1
        value2 <- opt2
      } yield value1 + value2
    }

    def div(opt1: Option[Int], opt2: Option[Int]): Option[Int] =  {
//      opt2 match {
//        case Some(0) => return None
//      }

      for {
        value1 <- opt1
        value2 <- opt2 if value2 != 0
      } yield value1 / value2
    }

    def subtract(opt1: Option[Int], opt2: Option[Int]): Option[Int] =  {
      for {
        value1 <- opt1
        value2 <- opt2
      } yield value1 - value2
    }

    def mult(opt1: Option[Int], opt2: Option[Int]): Option[Int] =  {
      for {
        value1 <- opt1
        value2 <- opt2
      } yield value1 * value2
    }

    operator match {
      case "+" => sum(op1, op2)
      case "-" => subtract(op1, op2)
      case "*" => mult(op1, op2)
      case "/" => div(op1, op2)
    }

//    op1 match {
//      case None => println("invalid input for the first operand")
//      case _ => op2 match {
//        case None => println("invalid input for the second operand")
//        case _ => res match {
//          case None => println("division by zero")
//          case Some(r) => println(r)
//        }
//      }
//    }
  }


}
