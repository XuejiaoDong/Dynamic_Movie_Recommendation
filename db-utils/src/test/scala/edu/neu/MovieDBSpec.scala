package edu.neu

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MovieDBSpec extends AnyFlatSpec with Matchers {

  behavior of "MovieDB"
  it should "work for info" in {

    val movie = MovieDB.info(1)
    movie should matchPattern {
      case Movie(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy", 0) =>
    }

  }
}
