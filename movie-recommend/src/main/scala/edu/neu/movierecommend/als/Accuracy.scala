package edu.neu.movierecommend.als

import edu.neu.movierecommend.als.Accuracy.MAX_DIFFERENCE

/**
 * This class represent the percentage of model accuracy
 *
 * - rmse means the average difference to the right rating,
 * - if rmse is 0, that means we get the exact right rating, so percent will be 100%
 * - if rmse is 5, that means we get a very bad rating, so percent will be 0%
 * - rmse could be negative, that means we are so far away from the right rating, which is not what want.
 *
 * @param rmse
 */
case class Accuracy(rmse: Double) {

  /**
   * convert the rmse score to accuracy of our model in percentage
   *
   * @return accuracy of our model in percentage
   */
  def percent: String = math.round((1 - rmse / MAX_DIFFERENCE) * 100.0) + "%"

  def toDouble: Double = math.round((1 - rmse / MAX_DIFFERENCE) * 100.0) / 100.0
}

case object Accuracy {
  /**
   * We define the max difference between the actual rating and predicted rating is 5,
   * because the max rating is 5
   */
  val MAX_DIFFERENCE = 5
}
