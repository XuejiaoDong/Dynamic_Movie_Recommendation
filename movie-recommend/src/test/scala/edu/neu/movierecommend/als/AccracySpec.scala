package edu.neu.movierecommend.als

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AccracySpec extends AnyFlatSpec with Matchers {

  behavior of "Accuracy"
  it should "work for percent" in {
      Accuracy(1.40).percent shouldBe "72%"
      Accuracy(1.42).percent shouldBe "72%"
  }
}
