package eud.neu.etl

import edu.neu.etl.MovieLog
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MovieLogSpec extends AnyFlatSpec with Matchers {

  behavior of "MovieLog"
  it should "work for parse" in {

    val movie = MovieLog.parse("1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy")
    movie should matchPattern {
      case MovieLog(1, "Toy Story (1995)", "Adventure|Animation|Children|Comedy|Fantasy") =>
    }

  }
}
