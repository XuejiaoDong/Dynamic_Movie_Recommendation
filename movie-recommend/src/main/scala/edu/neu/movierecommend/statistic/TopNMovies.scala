package edu.neu.movierecommend.statistic

import edu.neu.redis.RedisUtils
import org.apache.spark.sql.Dataset

case object TopNMovies {

  val TOP_N = 50 // todo: 50

  /**
   * Used as key in redis
   */
  val redisKey: String = "TopNMovies"

  def save(movieAvgRatings: Seq[MovieAvgRating]): Boolean = {

    val movieAvgRatingString = movieAvgRatings.mkString(",")

    RedisUtils.set(redisKey, movieAvgRatingString)
  }

  def movies: Option[String] = {
    RedisUtils.get(redisKey)
  }

}
