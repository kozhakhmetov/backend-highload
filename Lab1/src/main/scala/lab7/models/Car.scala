package lab7.models

case class Car(id: Option[Int] = None, brand: String, model: String, year: Int, color: String, maxSpeed: Int, carType: String, cost: Int)
