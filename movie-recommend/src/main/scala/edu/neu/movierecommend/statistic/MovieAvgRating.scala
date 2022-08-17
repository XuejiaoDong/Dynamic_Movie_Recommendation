package edu.neu.movierecommend.statistic

case class MovieAvgRating(movieId: Int, avgRating: Double) {

  override def toString: String = movieId + ":" + math.round(avgRating * 10.0) / 10.0
}


case object MovieAvgRating {

  implicit object MovieAvgRatingOrdering extends Ordering[MovieAvgRating] {
    override def compare(x: MovieAvgRating, y: MovieAvgRating): Int = y.avgRating.compare(x.avgRating)
  }

}

