package lab7

import akka.actor.{Actor, ActorLogging, Props}
import slick.jdbc.{PostgresProfile}

import slick.jdbc.PostgresProfile.api._
import lab7.models.{Car, Cars}
import slick.jdbc

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Billionaire {

  case class BuyCar(brand: String, model: String, year: Int, color: String, maxSpeed: Int, carType: String, cost: Int)

  case object GetCars // done

  case class UpdateCar(id: Int, brand: String, model: String, year: Int, color: String, maxSpeed: Int, carType: String, cost: Int)

  case class DeleteCar(id: Int)

  case class GetCar(id: Int)


  def props(db: PostgresProfile.backend.Database) = Props(new Billionaire(db))
}


class Billionaire(db: PostgresProfile.backend.Database) extends Actor {

  import Billionaire._

  val carsTable = TableQuery[CarsTable]

  val cars: Seq[Car] = Await.result(db.run(carsTable.result), 5.seconds)

  override def postStop(): Unit = println("Billionaire has stopped")

  override def preStart(): Unit = println("Billionaire has started")

  override def postRestart(reason: Throwable): Unit = println("Billionaire has restarted")

  override def receive: Receive = {
    case GetCars =>
      sender() ! Cars(Await.result(db.run(carsTable.result), 5.seconds))

    case GetCar(id) =>
      //sender() ! Car(db.run(carsTable.filter(_.carId === id).))

    case BuyCar(newBrand, newModel, newYear, newColor, newMaxSpeed, newCarType, newCost) =>
      sender() ! db.run(
        carsTable += Car(brand = newBrand, model = newModel, year = newYear, color = newColor, maxSpeed = newMaxSpeed,
          carType = newCarType, cost = newCost)
      )

    case UpdateCar(id, newBrand, newModel, newYear, newColor, newMaxSpeed, newCarType, newCost) =>
      sender() ! db.run(
        carsTable.filter(_.carId === id).update(Car(Some(id), brand = newBrand, model = newModel, year = newYear, color = newColor, maxSpeed = newMaxSpeed,
          carType = newCarType, cost = newCost))
      )

    case DeleteCar(id) =>
      sender() ! db.run(
        carsTable.filter(_.carId === id).delete
      )
  }

}
