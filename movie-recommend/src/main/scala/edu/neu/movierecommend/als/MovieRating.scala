package edu.neu.movierecommend.als

import org.apache.spark.mllib.recommendation.Rating

/**
 * This class represents a Movie Rating from rating.csv.
 *
 * The field name is same as the column name in rating.csv
 *
 * @param userId    user id
 * @param movieId   movie id
 * @param rating    rating score
 * @param timestamp timestamp
 */
case class MovieRating(userId: Int, movieId: Int, rating: Double, timestamp: Int) {
  /**
   * Convert MovieRating object to ALS rating object which is used in ALS training
   *
   * @return ALS Rating object
   */
  def toRating: Rating = Rating(userId, movieId, rating)
}

/**
 * This class presents an recommended movie with its rating
 *
 * @param movieId movie id
 * @param rating  rating
 */
case class Recommendation(movieId: Int, rating: Double) {
  override def toString: String = movieId.toString + ":" + math.round(rating * 100.0) / 100.0
}

/**
 * This object is Recommendation companion object.
 */
case object Recommendation {

  /**
   * This implicit object is used to sort Recommendation objects in descending order on rating.
   * The recommendation with highest rating will the first element.
   */
  implicit object RecommendationOrdering extends Ordering[Recommendation] {
    override def compare(x: Recommendation, y: Recommendation): Int = y.rating.compare(x.rating)
  }

}

/**
 * This class represents recommended movies for a user
 *
 * @param userId          user id
 * @param recommendations recommended movies
 */
case class UserRecommendation(userId: Int, recommendations: Seq[Recommendation]) {

  /**
   * This method will return a tuple, the first item is the userId,
   * the second item is movieIds joined by `,`.
   *
   * It is used when we want to same this object to redis or other KV cache service
   *
   * @return a tuple (userId, movieIds)
   */
  //def kv: (Int, String) = userId -> recommendations.map(_.movieId).mkString(",")
  def kv: (Int, String) = userId -> recommendations.map(_.toString).mkString(",")

}

/**
 * This object is UserRecommendation companion object
 */
case object UserRecommendation {
  /**
   * Used as key in redis
   */
  val redisKey: String = "userRecs"
}
