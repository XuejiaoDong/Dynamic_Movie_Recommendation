package edu.neu.movierecommend.als

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * This class represents the hyper parameter in ALS training
 *
 * @param rank       number of features to use (also referred to as the number of latent factors)
 * @param iterations number of iterations of ALS
 * @param lambda     regularization parameter
 * @param rmse       Root Mean Square Error, which is used to evaluate performance of a model
 */
case class HyperParameterRMSE(rank: Int, iterations: Int, lambda: Double, rmse: Double) {

  /**
   * use a tuple to wraps rank, iterations, lambda, rmse and return this tuple
   *
   * @return a tuple wraps rank, iterations, lambda, rmse
   */
  def tuple: (Int, Int, Double, Double) = (rank, iterations, lambda, rmse)
}

case object HyperParameterRMSE {

  /**
   * This implicit object is used to sort HyperParameterRMSE objects in descending order on RMSE score.
   * The HyperParameterRMSE object with lowest RMSE will the first element.
   */
  implicit object HyperParameterRMSEOrdering extends Ordering[HyperParameterRMSE] {
    override def compare(x: HyperParameterRMSE, y: HyperParameterRMSE): Int = x.rmse.compare(y.rmse)
  }

}

/**
 * ALS Grid Search algorithm:
 * We use RMSE (Root Mean Square Error) to evaluate model with different hyper parameter.
 * The best model hyper parameter has the lowest RMSE
 */
object GridSearch {


  /**
   * Calculate RMSE(Root Mean Square Error):
   *
   * 1. use machine learning model to get the predicted rating on the test dataset
   *
   * 2. use spark sql API to join the test dataset and predict dataset on the same user and product,
   * so we get a new dataset whose fields are (user, product, test.rating, product.rating)
   *
   * 3. use spark sql API (selectExpr) to calculate the RMSE(Root Mean Square Error) of
   * actual rating and predicted rating
   *
   * @param model  machine learning model
   * @param testDS test dataset
   * @return RMSE score
   */
  def rootMeanSquareError(spark: SparkSession, model: MatrixFactorizationModel, testDS: Dataset[Rating]): Double = {

    import spark.implicits._

    // predict on testDS
    val usersProducts = testDS.map(x => (x.user, x.product)).rdd
    val predictRatingDS = model.predict(usersProducts).toDS()

    // join test dataset and predict dataset and then calculate RMSE(Root Mean Square Error)
    val test = testDS.as("test")
    val predict = predictRatingDS.as("predict")
    val result = test.join(predict, Seq("user", "product"))
      .selectExpr("sqrt(avg(pow(test.rating - predict.rating, 2))) as rmse")

    //result.show() // todo:show
    result.first().getAs("rmse") // RMSE: value of first row
  }


  /**
   * Use grid search to find the best hyper parameter.
   *
   * The best hyper parameter has lowest RMSE
   *
   * @param trainDS train dataset
   * @param testDS  test dataset
   * @return the best hyper parameter including its RMSE
   */
  def gridSearch(spark: SparkSession, trainDS: Dataset[Rating], testDS: Dataset[Rating]): HyperParameterRMSE = {
    val hyperParams = for {
      //      rank <- Array(20, 50, 100, 150, 200)
      //      iterations <- Array(5, 50, 100, 200)
      //      lambda <- Array(0.01, 0.05, 0.1, 0.15, 1)

      rank <- Array(50)
      iterations <- Array(80)
      lambda <- Array(0.1)

      model = ALS.train(trainDS.rdd, rank, iterations, lambda, 16)
      rmse = rootMeanSquareError(spark, model, testDS)
    } yield HyperParameterRMSE(rank, iterations, lambda, rmse)

    println("hyperParams=" + hyperParams.toList)
    hyperParams.min // return the hyper parameter with lowest RMSE which is best hyper parameter
  }

  /**
   * Find the best hyper parameter
   *
   * @param ALSRatingDS
   * @return the best hyper parameter
   */
  def bestHyperParameter(spark: SparkSession, ALSRatingDS: Dataset[Rating]): HyperParameterRMSE = {

    // split dataset to trainDS(0.8), testDS(0.2)
    val Array(trainDS, testDS) = ALSRatingDS.randomSplit(Array(0.8, 0.2))
    // use grid search to find the best hyper parameter
    gridSearch(spark, trainDS, testDS)
  }

}
