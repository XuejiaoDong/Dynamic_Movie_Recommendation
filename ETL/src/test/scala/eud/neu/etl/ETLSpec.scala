package eud.neu.etl

import edu.neu.etl.ETLSparkApp
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ETLSpec extends AnyFlatSpec with Matchers {

  behavior of "ETL"
  it should "have date in ratingDf" in {
    ETLSparkApp.ratingDS should not be null
  }
}
