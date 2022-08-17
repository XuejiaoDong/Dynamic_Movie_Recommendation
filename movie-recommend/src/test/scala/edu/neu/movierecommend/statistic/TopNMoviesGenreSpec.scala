package edu.neu.movierecommend.statistic

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TopNMoviesGenreSpec extends AnyFlatSpec with Matchers {

  behavior of "TopNMoviesGenre"
  it should "work for save and movies" in {

    val comedy = TopNMoviesGenre("Comedy", Seq(MovieAvgRating(1, 4.12345), MovieAvgRating(2, 4.1512)))
    val action = TopNMoviesGenre("Action", Seq(MovieAvgRating(3, 4.12345), MovieAvgRating(4, 4.1512)))

    val topNMoviesGenre: Seq[TopNMoviesGenre] = Seq(comedy, action)

    TopNMoviesGenre.save(topNMoviesGenre) shouldBe true

    TopNMoviesGenre.movies("ComeDY") should matchPattern {
      case Some("1:4.1,2:4.2") =>
    }

    TopNMoviesGenre.movies("Action") should matchPattern {
      case Some("3:4.1,4:4.2") =>
    }

  }

}
