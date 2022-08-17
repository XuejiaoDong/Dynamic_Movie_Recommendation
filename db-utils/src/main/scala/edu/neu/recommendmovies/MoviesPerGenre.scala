package edu.neu.recommendmovies

import edu.neu.{Movie, MovieDB}
import edu.neu.redis.RedisUtils

case object MoviesPerGenre {
  def topNMoviesPerGenre(genre: String, num: Int): Seq[Movie] = {

    // read top N movies of this genre from redis
    val genreKey = genre.toLowerCase
    val mo: Option[Map[String, String]] = RedisUtils.hmget("TopNMoviesGenre", genreKey)
    val movies: Seq[Movie] = mo match {
      case None => Seq()
      case Some(map) =>
        val movieIdRatings = map(genreKey)
        for {
          movieIdRating <- movieIdRatings.split(",")
          Array(movieId, rating) = movieIdRating.split(":")
          movieInfo = MovieDB.info(movieId)
        } yield Movie(movieId.toInt, movieInfo.title, movieInfo.genres, rating.toDouble)
    }

    movies.take(num)

  }

}
