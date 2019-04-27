package HotelBooking


import HotelBooking.actors.Hotel
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import HotelBooking.models.ErrorInfo


trait JSONSupport {
  implicit val roomModelFormat: RootJsonFormat[models.RoomModel] = jsonFormat4(models.RoomModel)
  implicit val errorInfoFormat: RootJsonFormat[ErrorInfo] = jsonFormat2(ErrorInfo)
  implicit val hotelResponseFormat: RootJsonFormat[Hotel.Response] = jsonFormat1(Hotel.Response)
  //implicit val hoteRoomsFormat = jsonFormat1(Hotel.GetRooms)
  implicit val guestModelFormat: RootJsonFormat[models.GuestModel] = jsonFormat3(models.GuestModel)

}
