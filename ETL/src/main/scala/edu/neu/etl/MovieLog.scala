package edu.neu.etl

//case class MovieLog has 3 featureas: movieId,title,and genres
case class MovieLog(movieId: Int, title:String, genres:String) {

}

case object MovieLog {
  // def parse method
  def parse(x: String): MovieLog = {
    // split string by ","
    val xs = x.split(",")
    // parse these 3 parts into movieId,title, and genres
    MovieLog(xs(0).toInt, xs(1), xs(2))
  }
}
