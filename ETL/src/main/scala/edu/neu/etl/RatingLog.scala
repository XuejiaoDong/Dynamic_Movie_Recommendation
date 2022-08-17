package edu.neu.etl

// case class RatingLog has 4 features: userId, movieId, rating, timestamp
case class RatingLog(userId: Int, movieId: Int, rating: Double, timestamp: String){

}

case object RatingLog {
  // def parse method
  def parse(x: String): RatingLog = {
    // split string by ","
    val xs = x.split(",")
    //parse this 4 parts into userId,movieId,rating,and timestamp
    RatingLog(xs(0).toInt, xs(1).toInt, xs(2).toDouble, xs(3))
  }
}
