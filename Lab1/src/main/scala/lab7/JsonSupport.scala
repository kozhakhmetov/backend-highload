package lab7

import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._
import lab7.models.{Car, Response, Cars}

trait JsonSupport {
  implicit val carFormat: RootJsonFormat[Car] = jsonFormat8(Car)
  implicit val responseFormat: RootJsonFormat[Response] = jsonFormat1(Response)
  implicit val carsFormat: RootJsonFormat[Cars] = jsonFormat1(Cars)
}
