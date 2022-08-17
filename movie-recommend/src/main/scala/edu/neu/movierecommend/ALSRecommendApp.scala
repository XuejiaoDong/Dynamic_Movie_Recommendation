package edu.neu.movierecommend

import edu.neu.movierecommend.als.{ALSTrain, Accuracy, GridSearch, MovieRating, Recommendation, UserRecommendation}
import edu.neu.redis.RedisUtils
import org.apache.spark.{SPARK_BRANCH, SparkConf}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Encoders, Row, SparkSession}
import org.jblas.DoubleMatrix

import scala.annotation.tailrec


/**
 * Offline Recommendation Service:
 *
 * Algorithm:
 * 1. user movie ratings matrix is a spark matrix, we could not directly use traditional machine
 * learning model on it
 *
 * 2. we use ALS (Alternating Least Squares) algorithm factorizes the ratings matrix `R` as the
 * product of two lower-rank matrices, `X` and `Y`, i.e. `X * Yt = R`, so that we can find the
 * latent factors which are included in X, Y matrices.
 *
 * 3. ALS relies on several hyper parameters
 * - rank: number of features to use (also referred to as the number of latent factors)
 * - iterations: number of iterations of ALS
 * - lambda: regularization parameter
 * - blocks: level of parallelism to split computation into
 *
 * 4. Different hyper parameter will lead to different model with different performance, so we
 * need to user Grid Search to find the best hyper parameter
 *
 * 5. we use RMSE (Root Mean Square Error) to evaluate performance of different model with different hyper parameter
 *
 */
object ALSRecommendApp extends App {

  @tailrec
  def timerTrain(count: Int): Any = {
    println(count + "th train")
    ALSTrain.train()
    Thread.sleep(1000L) // sleep 1 seconds
    timerTrain(count + 1)
  }

  timerTrain(1)
}
