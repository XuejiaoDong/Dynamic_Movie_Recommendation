package edu.neu.movierecommend.statistic

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MovieAvgRatingSpec extends AnyFlatSpec with Matchers {

  behavior of "MovieAvgRating"
  it should "work for toString" in {

    MovieAvgRating(1, 4.12345).toString shouldBe  "1:4.1"

    MovieAvgRating(2, 4.1512).toString shouldBe "2:4.2"
  }

}
