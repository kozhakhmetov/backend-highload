package lab7

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import lab7.models.{Car}


class CarsTable(tag: Tag) extends Table[Car](tag, "CARS") {
  def carId: Rep[Int] = column[Int]("CAR_ID", O.PrimaryKey, O.AutoInc)
  def brand: Rep[String] = column[String]("BRAND")
  def model: Rep[String] = column[String]("MODEL")
  def year: Rep[Int] = column[Int]("YEAR")
  def color: Rep[String] = column[String]("COLOR")
  def maxSpeed: Rep[Int] = column[Int]("MAX_SPEED")
  def carType: Rep[String] = column[String]("CAR_TYPE")
  def cost: Rep[Int] = column[Int]("COST")

  override def * :ProvenShape[Car] = (carId.?, brand, model, year, color, maxSpeed, carType, cost) <> (Car.tupled, Car.unapply)
}
