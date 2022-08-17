package edu.neu.mysql

import java.sql.{Connection, DriverManager}

import edu.neu.RSConfig

import scala.util.Try

/**
 * MySQL Utility:
 * Provide several MYSQL operations
 */

case object MySQLUtils {

  /**
   * Get mysql connection
   * You should config your mysql ip, port, username, password here
   *
   * @return mysql connection
   */
  def getConnection: Connection = {
    val url = "jdbc:mysql://%s/recsys".format(RSConfig.mysql_server)
    val driver = "com.mysql.jdbc.Driver"
    val username = RSConfig.mysql_username
    val password = RSConfig.mysql_password

    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  /**
   * Execute insert or update SQL
   *
   * @param sql
   * @return
   */
  def insertOrUpdate(sql: String): Try[Int] = {

    Try {
      val connection = getConnection
      val statement = connection.createStatement
      val res = statement.executeUpdate(sql)
      connection.close()
      println("sql=%s, res=%s".format(sql, res))
      res
    }
  }

}