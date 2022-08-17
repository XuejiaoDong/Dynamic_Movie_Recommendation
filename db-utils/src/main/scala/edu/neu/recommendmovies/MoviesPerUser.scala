package edu.neu.recommendmovies

import edu.neu.{Movie, MovieDB}
import edu.neu.redis.RedisUtils

case object MoviesPerUser {

  def youMightLike(userId: Int, num: Int): Seq[Movie] = {

    val movies: Seq[Movie] = recommendedMovies(userId)
    val (needMore, numMore) = if (movies.size < num) (true, num - movies.size) else (false, 0)

    needMore match {
      case true => {
        movies ++ topNMoviesFromAll(numMore)
      }
      case _ => movies.take(num)
    }

  }

  /**
   * get recommended movies for specific user
   *
   * 1. first get recommended movies from redis,
   * the recommended movies in redis is in format movieId:predicted_rating
   * for example, "443:4.56,158972:4.51,8582:4.46",
   * 443 is movieId, 4.56 is its predicted rating
   *
   * 2. convert movieIds and Ratings to Movie instance with tile and genre which is got from MovieDB
   *
   * @param userId user id
   * @return the movies that recommended to this user
   */
  def recommendedMovies(userId: Int): Seq[Movie] = {

    // read recommended movies from redis
    val mo: Option[Map[String, String]] = RedisUtils.hmget("userRecs", userId.toString)
    mo match {
      case None => Seq()
      case Some(map) =>
        val movieIdRatings = map(userId.toString)
        for {
          movieIdRating <- movieIdRatings.split(",")
          Array(movieId, rating) = movieIdRating.split(":")
          movieInfo = MovieDB.info(movieId)
        } yield Movie(movieId.toInt, movieInfo.title, movieInfo.genres, rating.toDouble)
    }
  }

  def topNMoviesFromAll(num: Int): Seq[Movie] = {

    val so = RedisUtils.get("TopNMovies")
    val ms: Seq[Movie] = so match {
      case None => Seq()
      case Some(s) =>
        for {
          movieIdRating <- s.split(",")
          Array(movieId, rating) = movieIdRating.split(":")
          movieInfo = MovieDB.info(movieId)
        } yield Movie(movieId.toInt, movieInfo.title, movieInfo.genres, rating.toDouble)
    }
    ms take num
  }

}
