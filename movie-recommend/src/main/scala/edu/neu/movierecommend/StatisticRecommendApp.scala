package edu.neu.movierecommend

import edu.neu.movierecommend.als._
import edu.neu.movierecommend.statistic.{Movie, MovieAvgRating, MovieAvgRatingGenre, MovieGenre, TopNMovies, TopNMoviesGenre}
import edu.neu.redis.RedisUtils
import org.apache.spark.sql.{Dataset, Encoders, Row, SparkSession}
import org.apache.spark.sql.functions._


object StatisticRecommendApp extends App {

  // create spark session
  val spark: SparkSession = SparkSession
    .builder()
    .appName("StatisticRecommendApp")
    .master("local[*]")
    .getOrCreate()

  // ignore all of the INFO and WARN messages
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  // read rating.csv into a dataset
  val schema = Encoders.product[MovieRating].schema
  val movieRatingsDS: Dataset[MovieRating] = spark.read
    .option("header", true)
    .schema(schema) // passing schema
    .csv("../ETL/ETLRating/*.csv") // read all csv file in this path
    .as[MovieRating]

  println("movieRatingsDS")
  movieRatingsDS.show() // show sample in this dataset

  // Top N movies with highest rating from all history
  val movieAvgRating: Dataset[MovieAvgRating] = movieRatingsDS
    .groupBy("movieId")
    .agg((sum("rating") / count("rating")).as("avgRating"))
    .orderBy(desc("avgRating"))
    .as[MovieAvgRating]
    .as("movieAvgRating")

  println("movieAvgRating")
  movieAvgRating.show()

  // save Top N movies to redis
  val topNMovies = movieAvgRating.limit(TopNMovies.TOP_N)
  TopNMovies.save(topNMovies.collect())

  // Top N movies with highest rating for each genre
  val movieSchema = Encoders.product[Movie].schema
  val movies: Dataset[Movie] = spark.read
    .option("header", true)
    .schema(movieSchema)
    .csv("../db-utils/src/main/resources/movies.csv")
    .as[Movie]
    .as("movies")

  println("movies")
  movies.show()

  val movieGenres: Dataset[MovieGenre] = (for {
    movie <- movies
    genre <- movie.genres.split('|')
    movieId = movie.movieId
  } yield MovieGenre(movieId, genre))
    .as("movieGenres")

  println("movieGenres")
  movieGenres.show()

  val movieAvgRatingGenres = movieAvgRating.join(movieGenres, "movieId")
    .as[MovieAvgRatingGenre]

  // movieAvgRatingGenres.show()
  val topNMoviesGenre = movieAvgRatingGenres
    .map {
      case MovieAvgRatingGenre(movieId, avgRating, genre) => (genre, MovieAvgRating(movieId, avgRating))
    }
    .rdd
    .groupByKey()
    .map {
      case (genre, movieAvgRatings) => TopNMoviesGenre(genre, movieAvgRatings.toList.sorted.take(TopNMoviesGenre.TOP_N))
    }
    .toDS()


  // save to redis
  TopNMoviesGenre.save(topNMoviesGenre.collect())

  spark.stop()

}
