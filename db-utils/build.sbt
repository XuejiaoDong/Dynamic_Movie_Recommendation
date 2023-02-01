name := "db-utils"

version := "0.1"

scalaVersion := "2.12.15"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.3" % "test",
  "com.redislabs" %% "spark-redis" % "6.2.6",
  "net.debasishg" %% "redisclient" % "3.41",
  "mysql" % "mysql-connector-java" % "8.0.28",
  "com.typesafe" % "config" % "1.4.1",
)
