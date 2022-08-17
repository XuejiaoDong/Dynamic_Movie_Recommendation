package edu.neu.recommendmovies

import edu.neu.Movie
import edu.neu.redis.RedisUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MoviePerUserSpec extends AnyFlatSpec with Matchers {

  behavior of "MoviePerUser"
  it should "work for recommendedMovies" in {

    val kvs: Array[(Int, String)] = Array((1, "1:4.5,2:4.0"), (2, "3:4.5,4:4.0"))
    RedisUtils.hmset("userRecs", kvs)

    MoviesPerUser.recommendedMovies(1) should matchPattern {
      case Seq(
      Movie(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy", 4.5),
      Movie(2, "Jumanji (1995)", "Adventure|Children|Fantasy", 4.0),
      ) =>
    }
  }


  it should "work for topNMoviesFromAll" in {

    val movies = "1:4.5,2:4.0,3:3.5"
    RedisUtils.set("TopNMovies", movies)

    MoviesPerUser.topNMoviesFromAll(5) should matchPattern {
      case Seq(
      Movie(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy", 4.5),
      Movie(2, "Jumanji (1995)", "Adventure|Children|Fantasy", 4.0),
      Movie(3, "Grumpier Old Men (1995)", "Comedy|Romance", 3.5)
      ) =>
    }

    MoviesPerUser.topNMoviesFromAll(1) should matchPattern {
      case Seq(
      Movie(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy", 4.5)
      ) =>
    }

  }

}
