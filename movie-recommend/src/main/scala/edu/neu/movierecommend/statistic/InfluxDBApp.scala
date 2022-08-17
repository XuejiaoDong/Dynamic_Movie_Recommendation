package edu.neu.movierecommend.statistic

import io.razem.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object InfluxDBApp {


  val influxdb = InfluxDB.connect("localhost", 8086, "admin", "adminadmin")

  val database = influxdb.selectDatabase("initbucket")
  database.exists() // => Future[Boolean]
  database.create()
  database.drop()


  val point = Point("cpu")
    .addTag("host", "my.server")
    .addField("1m", 0.3)
    .addField("5m", 0.4)
    .addField("15m", 0.5)

  database.write(point) onComplete {
    case Success(result) =>
      println(result)
    case Failure(e) => println(e)
  }

  val rf: Future[QueryResult] = database.query("SELECT * FROM cpu")
  rf onComplete {
    case Success(result) =>
      println(result.series.head.records)
    case Failure(e) => println(e)
  }

//  Await.result(Duration.Inf)

  influxdb.close()

}
