package edu.neu.redis

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RedisUtilsSpec extends AnyFlatSpec with Matchers {

  behavior of "hmset and hmget"
  it should "work for hmset and hmget" in {

    val key = "userRecs"

    RedisUtils.hmset(key, Seq(1 -> "443:5.06,158972:4.99", 2 -> "443:4.93,8582:4.65"))

    val movieIds1 = for {
      m <- RedisUtils.hmget(key, 1)
      movieIds = m(1)
    } yield movieIds

    movieIds1 should matchPattern {
      case Some("443:5.06,158972:4.99") =>
    }

    val movieIds2 = for {
      m <- RedisUtils.hmget(key, 2)
      movieIds = m(2)
    } yield movieIds

    movieIds2 should matchPattern {
      case Some("443:4.93,8582:4.65") =>
    }

  }
}
