package HotelBooking


import HotelBooking.actors.Hotel
import HotelBooking.models.{GuestModel, RoomModel}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

trait Routes extends JSONSupport {
  implicit val timeout = Timeout(30.seconds)
  val roomRoute = path("room") {
    concat(
      post {
        entity(as[RoomModel]) { roomModel =>
          complete {
            // type cast to Library.Response
            //              "OK"
            // return type: String, Int, Standard library types, Future, Either, Option
            (Boot.hotel ? Hotel.CreateRoom(roomModel.id, roomModel.size, roomModel.number, roomModel.classOfRoom))
              .mapTo[Either[models.ErrorInfo, Hotel.Response]]
          }
        }
      },
      get {
        parameters("id".as[String]) { id =>
          complete {
            (Boot.hotel ? Hotel.GetRoom(id)).mapTo[Either[models.ErrorInfo, RoomModel]]
          }
        }
      }
    )
  }
  val guestRoute = path("guest") {
    post {
      entity(as[GuestModel]) {
        guestModel =>
          complete {
            (Boot.hotel ? Hotel.CreateGuest(guestModel.id, guestModel.firstName, guestModel.lastName))
              .mapTo[Either[models.ErrorInfo, Hotel.Response]]
          }
      }
    }
  }
}

object Boot extends App with Routes {
  val log = LoggerFactory.getLogger("Boot")

  // needed to run the route
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher


  val hotel = system.actorOf(Hotel.props(), "hotel")



  val route = pathPrefix("hotel") {
    concat(roomRoute, guestRoute)
  }



  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  log.info("Listening on port 8080...")
}
