package edu.neu.recommendmovies

import edu.neu.Movie
import edu.neu.redis.RedisUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MoviePerGenreSpec extends AnyFlatSpec with Matchers {

  behavior of "MoviePerGenre"
  it should "work for topNMoviesPerGenre" in {

    val kvs: Array[(String, String)] = Array(("comedy", "1:4.5,2:4.0"), ("romance", "3:4.5,4:4.0"))
    RedisUtils.hmset("TopNMoviesGenre", kvs)

    MoviesPerGenre.topNMoviesPerGenre("comedy", 10) should matchPattern {
      case Seq(
      Movie(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy", 4.5),
      Movie(2, "Jumanji (1995)", "Adventure|Children|Fantasy", 4.0),
      ) =>
    }

    MoviesPerGenre.topNMoviesPerGenre("romance", 10) should matchPattern {
      case Seq(
      Movie(3, "Grumpier Old Men (1995)", "Comedy|Romance", 4.5),
      Movie(4, "Waiting to Exhale (1995)", "Comedy|Drama|Romance", 4.0),
      ) =>
    }

  }
}
