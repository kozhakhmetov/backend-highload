import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{concat, pathPrefix}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import lab7.models.{Car, Cars, Response}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import lab7.{Billionaire, CarsTable, JsonSupport}
import slick.jdbc
import slick.jdbc.{PostgresProfile}

import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Boot extends App with JsonSupport {
  val log = LoggerFactory.getLogger("Boot")


  val config = ConfigFactory.load()
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val timeout = Timeout(30.seconds)
  val connectionUrl = "jdbc:postgresql://localhost/mydb?user=Adilkhan&password="


  val db: _root_.slick.jdbc.PostgresProfile.backend.DatabaseDef = Database.forURL(connectionUrl, driver = "org.postgresql.Driver")

  //val db:PostgresProfile.backend.Database = Database.forConfig("dbConfig")

  val cars = TableQuery[CarsTable]

  val billionaire = system.actorOf(Billionaire.props(db), "billionaire")

  val route =
    pathPrefix("billionaire") {
      path("cars") {
        get {
          complete {
            (billionaire ? Billionaire.GetCars).mapTo[Cars]
          }
        }
      }~
      path("car") {
        post {
          entity(as[Car]) { car =>
            complete {
              (billionaire ? Billionaire.BuyCar(car.brand, car.model, car.year, car.color,
                car.maxSpeed, car.carType, car.cost)).mapTo[Future[Int]].flatten.map (x => Response(x))
            }
          }
        }
      }~
      path("car" / IntNumber) { id => {
          put {
            entity(as[Car]) { car =>
              complete {
                (billionaire ? Billionaire.UpdateCar(id, car.brand, car.model, car.year, car.color,
                  car.maxSpeed, car.carType, car.cost)).mapTo[Future[Int]].flatten.map (x => Response(x))
              }
            }
          } ~ delete {
            complete {
              (billionaire ? Billionaire.DeleteCar(id)).mapTo[Future[Int]].flatten.map (x => Response(x))
            }
          } ~ get {
            complete {
              (billionaire ? Billionaire.GetCar(id)).mapTo[Car]
            }
          }
        }
      }
    }



  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  log.info("Listening on port 8080...")
}
