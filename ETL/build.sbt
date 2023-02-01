import sbt.Keys.libraryDependencies

name := "ETL"

version := "0.1"

scalaVersion := "2.12.15"

//scalacOptions in(Compile, doc) ++= Seq("-groups", "-implicits", "-deprecation", "-Ywarn-dead-code", "-Ywarn-value-discard", "-Ywarn-unused" )

//parallelExecution in Test := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.3" % "test",
  "org.apache.spark" %% "spark-core" % "3.2.0",
  "org.apache.spark" %% "spark-sql" % "3.2.0",
  "org.apache.spark" %% "spark-mllib" % "3.2.0",
  "org.jblas" % "jblas" % "1.2.3",
  "com.redislabs" %% "spark-redis" % "6.2.6",
  "net.debasishg" %% "redisclient" % "3.41",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.0",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.2.0",
  "org.apache.kafka" %% "kafka" % "3.0.0",
  "com.typesafe" % "config" % "1.4.1",
  "org.apache.kafka" % "kafka-clients" % "3.0.0",
  "mysql" % "mysql-connector-java" % "8.0.28",
)


//parallelExecution in Test := false
