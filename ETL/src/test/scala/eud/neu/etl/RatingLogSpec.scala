import edu.neu.etl.RatingLog
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RatingLogSpec extends AnyFlatSpec with Matchers {

  behavior of "RatingLog"
  it should "work for parse" in {

    val rating = RatingLog.parse("1,296,5.0,1147880044")
    rating should matchPattern {
      case RatingLog(1, 296, 5.0, "1147880044") =>
    }
  }
}
