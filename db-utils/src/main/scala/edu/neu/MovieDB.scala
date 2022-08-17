package edu.neu

import scala.io.Source

case class Movie(movieId: Int, title: String, genres: String, rating: Double = 0)

case object Movie {
  def parse(x: String): Movie = {
    val xs = x.split(",")
    Movie(xs(0).toInt, xs(1), xs(2))
  }
}

case object MovieDB {

  def info(movieId: Int): Movie = info(movieId.toString)

  def info(movieId: String): Movie = db(movieId)

  val db: Map[String, Movie] = {

    val rMovie1 = """^(.+),"(.+)",(.+)$""".r
    val rMovie2 = """^(.+),(.+),(.+)$""".r

    val src = Source.fromResource("movies.csv")

    val movies = for {
      l <- src.getLines()
      entry = l match {
        case rMovie1(movieId, title, genres) => movieId -> Movie(movieId.toInt, title, genres)
        case rMovie2(movieId, title, genres) => movieId -> Movie(movieId.toInt, title, genres)
      }
    } yield entry

     movies.toMap
  }


}
