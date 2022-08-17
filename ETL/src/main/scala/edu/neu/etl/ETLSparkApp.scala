package edu.neu.etl


import breeze.linalg.split
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, Encoders, SparkSession, functions}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}
import java.sql.DriverManager

import edu.neu.RSConfig


object ETLSparkApp extends App {

  /**--------------------create spark session----------------------------*/

  val spark: SparkSession = SparkSession
    .builder()
    .appName("ETLSparkApp")
    .master("local[*]")
    .getOrCreate()

  /**-------------ignore all of the INFO and WARN messages---------------*/

  spark.sparkContext.setLogLevel("ERROR")

  /**------------------read movie.csv to dataset-------------------------*/

  import spark.implicits._

  val schema = Encoders.product[MovieLog].schema

//  val movieDS: Dataset[MovieLog] = spark.read
//    .option("header", true)
//    .schema(schema) // passing schema
//    .csv("../db-utils/src/main/resources/ratings.csv")
//    .as[MovieLog]

  /**------------------read data stream from kafka----------------------*/

  val df = spark
    .readStream
    .format("kafka")
    // config kafka port
    .option("kafka.bootstrap.servers", RSConfig.kafka_server)
    //.option("kafka.bootstrap.servers", "192.168.0.107:9092")
    // .option("zookeeper.connect", "localhost:2181")
    //.option("startingOffsets", "earliest")  
    // config kafka topic
    .option("subscribe", "ratings")
    .load()

  // print kafka data schema
  df.printSchema()

  /** convert raw data from kafka to Dataset[RatingLog] (split value column value into multiple columns) * */

  import spark.implicits._

  val ratingDS = df.selectExpr("CAST(value as STRING)")
    .as[(String)]
    // invoke the parse function of RatingLog
    .map(RatingLog.parse(_))
    .as("rating")

  // print schema
  ratingDS.printSchema()

  //change ratingDf properties
  ETLCommons.setNullableStateOfColumn(ratingDS, "userId", true)
  ETLCommons.setNullableStateOfColumn(ratingDS, "movieId", true)
  ETLCommons.setNullableStateOfColumn(ratingDS, "rating", true)
  ETLCommons.setNullableStateOfColumn(ratingDS, "timestamp", true)

  /**-------------------save dataframe to csv------------------------*/
  
  // Use Spark writeStream write dataset to csv
  ratingDS.writeStream
    .format("csv")
  // Trigger once every second
    .trigger(Trigger.ProcessingTime("1 seconds"))
  // Data output mode select append
    .outputMode("append")
  // Add checkpoint
    .option("checkpointLocation", "checkpoint")
 // .option("maxFilesPerTrigger", 1)
    .option("path", "ETLRating")
    .start()
  //  .awaitTermination()

  println("df.selectExpr----------")

  /** logging sample data in console */
  df.selectExpr( "CAST(value as STRING)")
    .as[(String)]
  // result write into console
    .writeStream.format("console")
  // output mode select append
    .outputMode("append")
    .start()
    .awaitTermination()

  /**------------ save to mysql --------------------------*/


//   // configuration to connect to mysql
//   val url = "jdbc:mysql://localhost:3306/recsys"
//   val username = "root"
//   val password = "123456"


//   ratingDS.createOrReplaceTempView("ratingTb")
//   val ratingData = spark.sql("SELECT * FROM ratingTb")
//   println("ratingData=" + ratingData)
//   ratingData.show()

//   import org.apache.spark.sql

//     ratingData.write
//     .format("jdbc")
//     .option("url",url)
//  // .option("dbtable",msql_table_name)
//     .option("user",username)
//     .option("password",password)
//     .mode("append")
//     .saveAsTable("output")



/**---------------------stop spark session-----------------------*/
  spark.stop()
}
