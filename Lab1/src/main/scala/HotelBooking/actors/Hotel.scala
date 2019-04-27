package HotelBooking.actors

import HotelBooking.models.{ErrorInfo, GuestModel, RoomModel}
import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout}

object Hotel {
  def props() = Props(new Hotel())

  case class CreateRoom(id: String, size: Int, number: String, classOfRoom: String) // done

  case object GetRooms

  case class GetRoom(id: String) // done

  case class GetGuest(id: String) // done

  case class CreateGuest(id: String, firstName: String, lastName: String) // done

  case object GetGuests

  case class CheckIn(guestID : String, roomID : String)

  case class SetFreeRoom(id: String) // done

  case class Response(message: String) // response

  case class Rooms(rooms: Seq[RoomModel])

  case class Guests(guests: Seq[GuestModel])

}

class Hotel() extends Actor with ActorLogging {
  //import Hotel._

  var guests = Map.empty[String, ActorRef]
  var rooms = Map.empty[String, ActorRef]


  override def receive: Receive = {
    case Hotel.CreateRoom(id, size, number, classOfRoom) =>
      log.info(s"Create room with id $id received")
      if (rooms.contains(id)) {
        log.info("Room with such id already exists")
        sender() ! Left(ErrorInfo(409, s"Room with id: $id already exists"))
      } else {
        val room: ActorRef = context.actorOf(Room.props(id, size, number, classOfRoom), id)
        rooms = rooms + (id -> room)
        sender() ! Right(Hotel.Response("OK"))
      }

    case Hotel.CreateGuest(id: String, firstName: String, lastName: String) =>
      log.info(s"Create guest with id $id received")
      if (guests.contains(id)) {
        log.info("Guest with such id already exists")
        sender() ! Left(ErrorInfo(409, s"Guest with id: $id already exists"))
      } else {
        val guest: ActorRef = context.actorOf(Guest.props(id, firstName, lastName))
        guests = guests + (id -> guest)
        sender() ! Right(Hotel.Response("OK"))
      }

    case Hotel.GetRoom(id: String) =>
      log.info(s"Get room with id $id received")
      rooms.get(id) match {
        case Some(roomActor) =>
          roomActor ! Room.GetData
          context.become(waitingRoomResponse(sender()))
        case None => sender() ! Left(ErrorInfo(404, s"Room with id: $id not found."))
      }

    case Hotel.GetGuest(id: String) =>
      log.info(s"Get guest with id $id received")
      guests.get(id) match {
        case Some(guestActor) =>
          guestActor ! Guest.GetData
          context.become(waitingGuestResponse(sender()))
        case None => sender() ! Left(ErrorInfo(404, s"Guest with id: $id not found."))
      }

    case Hotel.SetFreeRoom(id: String) =>
      log.info(s"Empty room with id $id")
      rooms.get(id) match {
        case Some(roomActor) =>
          roomActor ! Room.SetFree
        case None => sender() ! Left(ErrorInfo(404, s"Room with id: $id not found."))
      }

    case Hotel.CheckIn(guestID: String, roomID: String) =>
      if (guests.contains(guestID) && rooms.contains(roomID)) {
        val roomActor = rooms.get(roomID) match {
          case Some(roomActor) => roomActor
        }
        val guestActor = guests.get(guestID) match {
          case Some(guestActor) => guestActor
        }
        roomActor ! Room.CheckInGuest(guestID, guestActor)
        context.become(waitingCheckInResponse(sender()))
      }else {
        sender() ! Left(ErrorInfo(404, s"No room with id: $roomID or guest with id: $guestID was found"))
      }

    case Hotel.GetRooms =>
      log.info("Received GetBooks")
      // send to all books GetData
      rooms.values.foreach(roomActor => roomActor ! Room.GetData)
      context.become(waitingResponsesGetRooms(rooms.size, sender(), Seq.empty[RoomModel]))


    case Hotel.GetGuests =>
      log.info("Receved GetGuests")
      guests.values.foreach(guestActor => guestActor ! Guest.GetData)
      context.become(waitingResponsesGetGuests(guests.size, sender(), Seq.empty[GuestModel]))
  }


  def waitingResponsesGetGuests(responsesLeft: Int, replyTo: ActorRef, guests: Seq[GuestModel]): Receive = {
    case guest: GuestModel =>
      //log.info(s"Received BookModel with name: ${room.}. Responses left: $responsesLeft")
      if (responsesLeft - 1 == 0) {
        log.info("All responses received, replying to initial request.")
        replyTo ! Hotel.Guests(guests :+ guest)
        context.become(receive)
      }
      else context.become(waitingResponsesGetGuests(responsesLeft - 1, replyTo, guests = guests :+ guest))
  }

  def waitingResponsesGetRooms(responsesLeft: Int, replyTo: ActorRef, rooms: Seq[RoomModel]): Receive = {
    case room: RoomModel =>
      //log.info(s"Received BookModel with name: ${room.}. Responses left: $responsesLeft")
      if (responsesLeft - 1 == 0) {
        log.info("All responses received, replying to initial request.")
        replyTo ! Hotel.Rooms(rooms :+ room)
        context.become(receive)
      }
      else context.become(waitingResponsesGetRooms(responsesLeft - 1, replyTo, rooms = rooms :+ room))
  }

  def waitingCheckInResponse(replyTo: ActorRef): Receive = {
    case Guest.InRoom(false) => replyTo ! Right(Hotel.Response("Checked in"))
      context.become(receive)

    case Guest.InRoom(true) => replyTo ! Left(ErrorInfo(1234, "guest already in some other room"))
      context.become(receive)

    case Room.RoomIsFull => replyTo ! Left(ErrorInfo(1235, "room is full"))
      context.become(receive)

    //case ErrorInfo(_, _) => replyTo ! ErrorInfo(_, _)

    case ReceiveTimeout => replyTo ! Left(ErrorInfo(504, "Timeout when looking for book."))


  }

  def waitingGuestResponse(replyTo: ActorRef): Receive = {
    case guest: GuestModel =>
      replyTo ! Right(guest)
      context.become(receive)

    case ReceiveTimeout =>
      replyTo ! Left(ErrorInfo(504, "Timeout when looking for book."))
  }

  def waitingRoomResponse(replyTo: ActorRef): Receive = {
    case room: RoomModel =>
      replyTo ! Right(room)
      context.become(receive)

    case ReceiveTimeout =>
      replyTo ! Left(ErrorInfo(504, "Timeout when looking for book."))
  }

}
