package edu.neu

import java.util.Properties

import com.typesafe.config.ConfigFactory

import scala.io.Source

/**
 * This object will load all system configuration including kafka, redis, mysql
 */
object RSConfig {

  /**
   * This properties includes all config
   */
  val properties: Properties = {

    val source = Source.fromResource("application.properties")
    val properties: Properties = new Properties()
    properties.load(source.bufferedReader())
    properties
  }

  val kafka_server: String = properties.getProperty("kafka.server")
  val redis_server: String = properties.getProperty("redis.server")
  val redis_port: Int = properties.getProperty("redis.port").toInt
  val mysql_server: String = properties.getProperty("mysql.server")
  val mysql_username: String = properties.getProperty("mysql.username")
  val mysql_password: String = properties.getProperty("mysql.password")

  println("loading system config:" + properties)

}
