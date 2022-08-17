package edu.neu.movierecommend.statistic

import org.apache.spark.sql.Dataset
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TopNMoviesSpec extends AnyFlatSpec with Matchers {

  behavior of "TopNMovies"
  it should "work for save and movies" in {

    val movieAvgRatings = Seq(MovieAvgRating(1, 4.12345), MovieAvgRating(2, 4.1512))

    TopNMovies.save(movieAvgRatings) shouldBe true

    TopNMovies.movies should matchPattern {
      case Some("1:4.1,2:4.2") =>
    }
  }

}
