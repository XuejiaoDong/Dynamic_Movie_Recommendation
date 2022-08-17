package edu.neu.movierecommend.als

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ALSTrainSpec extends AnyFlatSpec with Matchers {

  behavior of "ALSTrain"

  it should "work for saveRMSEAccuracy" in {

    ALSTrain.saveRMSEAccuracy(1, Accuracy(1)) shouldBe true
  }
}
