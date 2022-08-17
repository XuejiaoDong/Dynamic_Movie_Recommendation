package edu.neu.movierecommend.statistic

import edu.neu.redis.RedisUtils

case class Movie(movieId: Int, title: String, genres: String)

case class MovieGenre(movieId: Int, genre: String)

case class MovieAvgRatingGenre(movieId: Int, avgRating: Double, genre: String)

case class TopNMoviesGenre(genre: String, movieAvgRatings: Seq[MovieAvgRating]) {
  def kv: (String, String) = genre.toLowerCase -> movieAvgRatings.map(_.toString).mkString(",")
}

case object TopNMoviesGenre {

  val TOP_N = 50 // todo: 50

  val redisKey: String = "TopNMoviesGenre"

  def save(topNMoviesGenre: Seq[TopNMoviesGenre]): Boolean = {

    val kvs = topNMoviesGenre.map(_.kv)

    kvs match {
      case Seq() => true
      case _ => RedisUtils.hmset(redisKey, kvs)
    }
  }

  def movies(genre: String): Option[String] = {
    val genreKey = genre.toLowerCase
    val mo: Option[Map[String, String]] = RedisUtils.hmget(TopNMoviesGenre.redisKey, genreKey)
    for {
      m <- mo
      movieIds = m(genreKey)
    } yield movieIds
  }

}
