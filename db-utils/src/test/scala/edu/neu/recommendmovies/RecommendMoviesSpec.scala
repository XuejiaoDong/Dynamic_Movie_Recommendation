package edu.neu.recommendmovies

import edu.neu.redis.RedisUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RecommendMoviesSpec extends AnyFlatSpec with Matchers {

  behavior of "movies"
  it should "work for movies" in {

    RedisUtils.hmset("userRecs", Seq(1 -> "443:5.06,158972:4.99", 2 -> "443:4.93,8582:4.65"))


    val ms = RecommendMovies.youMightLike(1, 10)
    println(ms)

  }
}
