package edu.neu.movierecommend.statistic

import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import com.influxdb.client.scala.InfluxDBClientScalaFactory
import com.influxdb.query.FluxRecord

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InfluxDB1App {

  implicit val system: ActorSystem = ActorSystem("it-tests")

  // You can generate an API token from the "API Tokens Tab" in the UI
  val token = "e3ZygSg1_dcQMxyyL-PcBvWAKoUVadrsFqo86IMgpsMctUzC7G0rlSQHvMrZ28zSpcK8ng2hS7fxk4xsWYQE8w=="
  val org = "NEU"

  val client = InfluxDBClientScalaFactory.create("http://localhost:8086", token.toCharArray, org)


  client.getQueryScalaApi()


  val query =
    """from(bucket: "initbucket")
      |> range(start: -1d)
    """

  // Result is returned as a stream
  val results = client.getQueryScalaApi().query(query)

  val sink = results
    // print results
    .runWith(Sink.foreach[FluxRecord](it => println(s"$it")
    ))

  // wait to finish
  Await.result(sink, Duration.Inf)

  client.close()
  system.terminate()

}
