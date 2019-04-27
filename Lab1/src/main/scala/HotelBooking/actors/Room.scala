package HotelBooking.actors

import HotelBooking.models.{ErrorInfo, RoomModel}
import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout}

object Room {
  def props(id: String, size: Int, number: String, classOfRoom: String) = Props(new Room(id, size, number, classOfRoom))

  case class CheckInGuest(id: String, guestActor: ActorRef) // done

  case object SetFree // done

  case object GetData // done

  case class deleteGuest(id: String) // done

  case object RoomIsFull // done

}

class Room(id: String, size: Int, number: String, classOfRoom: String) extends Actor with ActorLogging {
  import Room._

  var guests = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case GetData => sender() ! RoomModel(id, size, number, classOfRoom)
    case SetFree => guests.values.foreach(guestActor => guestActor ! Guest.SetFree)
      guests = Map.empty[String, ActorRef]

    case deleteGuest(id: String) =>
      guests.get(id) match {
        case Some(guestActor) => guestActor ! Guest.SetFree
          guests = guests - id
        case None => println(s"No guest with id $id")
      }

    case CheckInGuest(id: String, guestActor: ActorRef) =>
      if (guests.size == size) {
        sender() ! RoomIsFull
      }else {
        guestActor ! Guest.SetRoom
        context.become(waitingGuestResponse(id, guestActor, sender()))
      }
    case _ => Actor.emptyBehavior
  }

  def waitingGuestResponse(guestID: String, guestActor: ActorRef, replyTo: ActorRef): Receive = {
    /*case Guest.InRoom(_) => replyTo ! Guest.InRoom(_)
      guests = guests + (guestID -> guestActor)
      context.become(receive)
*/
    case ReceiveTimeout =>
      replyTo ! Left(ErrorInfo(504, "Timeout when looking for book."))

  }


  override def preStart(): Unit = log.info(s"Room with id $id created")
}
