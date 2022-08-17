package edu.neu.movierecommend.als

import java.nio.file.{Files, Paths}
import java.sql.{Connection, DriverManager}

import edu.neu.mysql.MySQLUtils
import edu.neu.redis.RedisUtils
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Encoders, Row, SparkSession}

import scala.util.{Failure, Success, Try}

object ALSTrain {

  def train(): Unit = {
    // This config represents how many movies we wanna ML model to generate,
    // you can change it to what you want
    val MAX_RECOMMEND = 50 // todo: 50

    // create spark session
    val spark: SparkSession = SparkSession
      .builder()
      .appName("ALSRecommendApp") // give app a name
      .master("local[*]") // local[*] means it will use all your cpus of your computer
      .getOrCreate()

    // ignore all of the INFO and WARN messages
    spark.sparkContext.setLogLevel("ERROR")

    // set a checkpoint for this app, without it app will cause stackoverflow,
    // because ALS calculation will cost a lot stack memory
    spark.sparkContext.setCheckpointDir("als")

    import spark.implicits._

    // this is the datasource path, we will read rating data source from this file
    val path = "../ETL/ETLRating"

    // check if csv file exist,
    // if exist, we will execute training
    // if does not exist, we will return, and loop around to check again
    Try(csvExist(path)) match {
      case Success(true) => println("csv exist, start to read all csv file in ETL/ETLRating directory")
      case Failure(exception) =>
        println("csv does not exist, exception:" + exception)
        return
      case _ =>
        println("error")
        return
    }

    // read datasource

    // read rating.csv into a dataset
    val schema = Encoders.product[MovieRating].schema
    val movieRatingsDS: Dataset[MovieRating] = spark.read
      .schema(schema) // passing schema
      .csv("../ETL/ETLRating/*.csv")
      .as[MovieRating]

    movieRatingsDS.show(false) // show the sample of movieRating dataset

    // remove duplicated userId, movieId
    val userIdDS = movieRatingsDS.select("userId").distinct()
    val movieIdDS = movieRatingsDS.select("movieId").distinct()

    // convert movieRatings dataset to ALS Rating dataset which uses Rating provided by spark ALS
    val ALSRatingDS: Dataset[Rating] = movieRatingsDS
      .map {
        case MovieRating(userId, movieId, rating, timestamp) => Rating(userId, movieId, rating)
      }

    // use grid search to find best hyper parameter
    val hyperParameterRMSE = GridSearch.bestHyperParameter(spark, ALSRatingDS)
    println("best hyperParameterRMSE = " + hyperParameterRMSE)

    // calculate the accuracy
    val accuracy = Accuracy(hyperParameterRMSE.rmse)
    val accuracyPercent = accuracy.percent
    println("accuracyPercent=" + accuracyPercent)

    // save RMSE and accuracy to mysql in order to virtualize the performance of our model
    saveRMSEAccuracy(hyperParameterRMSE.rmse, accuracy)

    // train the whole dataset with best hyper parameter
    val (rank, iterations, lambda, rmse) = hyperParameterRMSE.tuple
    val model = ALS.train(ALSRatingDS.rdd, rank, iterations, lambda)


    // prepare user movie dataset to be predicted
    // create an empty rating matrix (userMovieDS) which is the cartesian join between userId dataset and movieId dataset
    val ratedUserMovieDF = movieRatingsDS.select("userId", "movieId")
    val userMovieDS: Dataset[Row] = userIdDS.crossJoin(movieIdDS) // cartesian join userId dataset and movieId dataset
      .except(ratedUserMovieDF) // remove already rated userMovies
    println("userMovieDS")
    userMovieDS.show()

    if (userMovieDS.isEmpty) {
      println("userMovieDS.isEmpty")
      return
    }


    // predict the ratings of every user on every movie
    val userMovies: RDD[(Int, Int)] = userMovieDS.rdd.map {
      case Row(userId: Int, movieId: Int) => (userId, movieId)
    }

    //  val predictRatingsRDD = model.predict(userMovies).toDS()
    //  val predictRatingsDS = spark.createDataset(predictRatingsRDD)
    val predictRatingsDS: Dataset[Rating] = model.predict(userMovies).toDS()
    //predictRatingsDS.show

    // user recommended movies
    val userRecsDS: Dataset[UserRecommendation] = predictRatingsDS
      .where("rating > 0") // choose rating great than 0
      .map {
        case Rating(user, product, rating) => (user, Recommendation(product, rating))
      }
      // .map(row => (row.user, Recommendation(row.product, row.rating)))
      .rdd
      .groupByKey()
      .map {
        case (userId, recommendations) => UserRecommendation(userId, recommendations.toList.sorted.take(MAX_RECOMMEND))
      }
      .toDS()

    println("userRecsDS")
    userRecsDS.show(false)

    // save user recommended movies to redis
    val kvs: Array[(Int, String)] = userRecsDS.map(_.kv).collect()
    println("kvs.length=" + kvs.length)

    kvs match {
      case Array() => println("len is 0")
      case _ =>
        println("len > 0")
        RedisUtils.hmset(UserRecommendation.redisKey, kvs)
    }

    RedisUtils.hmset(UserRecommendation.redisKey, kvs)

    spark.stop()
  }


  /**
   * save the RMSE and accuracy to mysql
   *
   * @param rmse
   * @param accuracy
   * @return true: success, false: fail
   */
  def saveRMSEAccuracy(rmse: Double, accuracy: Accuracy): Boolean = {

    val rmseRound = math.round(rmse * 100) / 100.0
    val sql = "INSERT INTO performance (rmse, accuracy) VALUES (%s, %s)".format(rmseRound, accuracy.toDouble)
    val it = MySQLUtils.insertOrUpdate(sql)
    it match {
      case Success(1) => true
      case Failure(e) =>
        println("saveRMSEAccuracy fail, because " + e)
        false
      case _ =>
        println("saveRMSEAccuracy fail, result is " + _)
        false
    }
  }


  def csvExist(path: String) = {
    val home = scala.reflect.io.File(path)
    home.exists match {
      case true =>
        val xs = for {
          f <- home.toDirectory.files
          x = f.name.endsWith(".csv")
        } yield x
        xs.contains(true)
      case false => false
    }
  }


}
