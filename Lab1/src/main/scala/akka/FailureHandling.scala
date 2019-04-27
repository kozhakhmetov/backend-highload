package akka

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn


object SupervisingActor {
  def props: Props = Props(new SupervisingActor)
}

class SupervisingActor extends Actor {
  val child = context.actorOf(SupervisedActor.props, "supervised-actor")

  override def receive: Receive = {
    case "failChild" ⇒ child ! "fail"
  }
}

object SupervisedActor {
  def props: Props =
    Props(new SupervisedActor)
}

class SupervisedActor extends Actor {
  override def preStart(): Unit = println("supervised actor started")
  override def postStop(): Unit = println("supervised actor stopped")

  override def receive: Receive = {
    case "fail" ⇒
      println("supervised actor fails now")
      throw new Exception("I failed!")
  }
}

object FailureHandling extends App{
  val system = ActorSystem("testSystem")
  val supervisingActor = system.actorOf(SupervisingActor.props, "supervising-actor")
  supervisingActor ! "failChild"

}
