package edu.neu.mysql

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.Success

class MySQLUtilsSpec extends AnyFlatSpec with Matchers {

  behavior of "insert"
  it should "work for insert" in {

    val sql = "INSERT INTO performance (rmse, accuracy) VALUES (4, 0.20)"
    MySQLUtils.insertOrUpdate(sql) should matchPattern {
      case Success(1) =>
    }

    val sql1 = "INSERT INTO input(amount) VALUES(2000)"
    MySQLUtils.insertOrUpdate(sql1) should matchPattern {
      case Success(1) =>
    }


  }
}
