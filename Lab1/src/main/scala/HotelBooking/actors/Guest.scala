package HotelBooking.actors

import HotelBooking.models.GuestModel
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Guest {
  def props(id: String, firstName: String, lastName: String) = Props(new Guest(id, firstName, lastName))

  case object GetData //

  case object SetFree //

  case object SetRoom //

  case class InRoom(isTrue: Boolean)
}

class Guest(id: String, firstName: String, lastName: String) extends Actor with ActorLogging {
  import Guest._

  var room: Option[ActorRef] = None

  override def receive: Receive = {
    case GetData => sender() ! GuestModel(id, firstName, lastName)

    case SetRoom =>
      room = Some(sender())
      sender() ! InRoom(false)
      context.become(inRoom)

    case _ => Actor.emptyBehavior
  }

  def inRoom: Receive = {
    case GetData => sender() ! GuestModel(id, firstName, lastName)

    case SetFree => room = None
      context.become(receive)

    case SetRoom =>
      sender() ! InRoom(true)

    case _ => Actor.emptyBehavior
  }
}
