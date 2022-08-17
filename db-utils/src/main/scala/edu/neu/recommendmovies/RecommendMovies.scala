package edu.neu.recommendmovies

import edu.neu.Movie

case object RecommendMovies {

  def youMightLike(userId: Int, num: Int = 10): Seq[Movie] = MoviesPerUser.youMightLike(userId, num)

  def topNMoviesPerGenre(genre: String, num: Int = 10): Seq[Movie] = MoviesPerGenre.topNMoviesPerGenre(genre, num)
}
