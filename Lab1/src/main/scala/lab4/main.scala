package lab4
import scala.math.min

object main extends App{

  case class Film( name: String,
                   yearOfRelease: Int,
                   imdbRating: Double)
  case class Director( firstName: String,
                       lastName: String,
                       yearOfBirth: Int,
                       films: Seq[Film])


  val memento = new Film("Memento", 2000, 8.5)
  val darkKnight = new Film("Dark Knight", 2008, 9.0)
  val inception = new Film("Inception", 2010, 8.8)
  val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7)
  val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9)
  val unforgiven = new Film("Unforgiven", 1992, 8.3)
  val granTorino = new Film("Gran Torino", 2008, 8.2)
  val invictus = new Film("Invictus", 2009, 7.4)
  val predator = new Film("Predator", 1987, 7.9)
  val dieHard = new Film("Die Hard", 1988, 8.3)
  val huntForRedOctober = new Film("The Hunt for Red October", 1990, 7.6)
  val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8)
  val eastwood = new Director("Clint", "Eastwood", 1930,
    Seq(highPlainsDrifter, outlawJoseyWales, unforgiven, granTorino, invictus))
  val mcTiernan = new Director("John", "McTiernan", 1951,
    Seq(predator, dieHard, huntForRedOctober, thomasCrownAffair))
  val nolan = new Director("Christopher", "Nolan", 1970,
    Seq(memento, darkKnight, inception))
  val someGuy = new Director("Just", "Some Guy", 1990,
    Seq())
  val directors = Seq(eastwood, mcTiernan, nolan, someGuy)

  // Task 1 Accept a parameter numberOfFilms of type Int — find all directors who have directed more than numberOfFilms
  def moreThan(numberOfFilms: Int): Seq[Director] = {
    directors.filter(director => director.films.size >= numberOfFilms)
  }
  //moreThan(3).foreach(println)
  // Task 2 Accept a parameter year of type Int — find a director who was born before that year
  def bornBeforeYear(year: Int): Seq[Director] = {
    directors.filter(director => director.yearOfBirth <= year)
  }
  //Task 3 Accept two parameters, year and numberOfFilms , and return a list of directors who were born before year who have also directed more than than numberOfFilms.
  def moreThanAndBeforeYear(numberOfFilms: Int, year: Int): Seq[Director] = {
    directors.filter(director => director.yearOfBirth <= year).filter(director => director.films.size >= numberOfFilms)
  }
  // Task 4 Accept a parameter ascending of type Boolean that defaults to true. Sort the directors by age in the specified order.
  def sort(ascending: Boolean = true): Seq[Director] = {
    directors.sortWith((x, y) => ascending match {
      case false => (x.yearOfBirth > y.yearOfBirth)
      case true => (x.yearOfBirth < y.yearOfBirth)
    })
  }
  // Task 5  Nolan films
  //
  //Starting with the definition of nolan, create a list containing the names of the films directed by Christopher Nolan.

  def NolamFilms(): Seq[String] = {
    nolan.films.map(film => film.name)
  }
  //Task 6 - Cinephile

  //Starting with the definition of directors, create a list containing the names of all films by all directors.
  def allFilms(): Seq[String] = {
    directors.flatMap(director => director.films.map(film => film.name))
  }
  //allFilms().foreach(println)

  // Task 7 Vintage McTiernan
  //
  //Starting with mcTiernan, find the date of the earliest McTiernan film.
  //Tip: you can concisely find the minimum of two numbers a and b using math.min(a, b) .
  def earliestFile() : Int = {
    mcTiernan.films.foldLeft(100000) ((minimum, i) => math.min(minimum, i.yearOfRelease))
  }
  //Task 8 - High Score Table
  def highScoreTable() : Seq[Film] = {
    directors.flatten(director => director.films).sortWith((x, y) => x.imdbRating > y.imdbRating)
  }
  //highScoreTable().foreach(println)


  //Task 9

  //Starting with directors again, find the average score across all films.
  def averageOfAll() : Double = {
    var films: Seq[Film] = directors.flatten(director => director.films)
    var sum: Double = films.foldLeft(0.0)((sum, film) => sum + film.imdbRating)
    return sum / films.size
  }

  //Task 10 - Tonight’s Listings

  //Starting with directors , print the following for every film: "Tonight only! FILM NAME by DIRECTOR!"

  def printTonightOnly() : Unit = {
    directors.foreach(director => director.films.foreach(film => println(s"Tonight only! ${film.name} by ${director.firstName} ${director.lastName}!")))
  }
  //printTonightOnly()

  //Task 11 - From the Archives
  //Finally, starting with directors again, find the earliest film by any director.

  //def printMin



}
