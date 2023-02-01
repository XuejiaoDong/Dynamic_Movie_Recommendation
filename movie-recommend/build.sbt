name := "movie-recommend"

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
  //  "com.redislabs" %% "spark-redis" % "2.4.2",
  "net.debasishg" %% "redisclient" % "3.41",
   "com.influxdb" % "influxdb-client-scala_2.12" % "4.0.0",
  "io.razem" %% "scala-influxdb-client" % "0.6.3",
  "mysql" % "mysql-connector-java" % "8.0.28",
)

//parallelExecution in Test := false
