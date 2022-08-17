package edu.neu.movierecommend.als

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MovieRatingSpec extends AnyFlatSpec with Matchers {

  // todo
  behavior of "MovieRating"

  behavior of "Recommendation"
  it should "work for toString" in {

    Recommendation(1, 4.12345).toString shouldBe  "1:4.12"

    Recommendation(2, 4.12512).toString shouldBe "2:4.13"
  }

  // todo
  behavior of "UserRecommendation"
  it should "work for kv" in {

    val ur = UserRecommendation(101, Seq(Recommendation(1, 4.12345), Recommendation(2, 4.125)))
    val kv = ur.kv
    kv should matchPattern {
      case (101, "1:4.12,2:4.13")=>
    }
  }



}
