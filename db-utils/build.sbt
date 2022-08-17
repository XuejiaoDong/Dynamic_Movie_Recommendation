name := "db-utils"

version := "0.1"

scalaVersion := "2.12.15"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.3" % "test",
  "com.redislabs" %% "spark-redis" % "2.4.2",
  "net.debasishg" %% "redisclient" % "3.41",
  "mysql" % "mysql-connector-java" % "5.1.24",
  "com.typesafe" % "config" % "1.4.1",
)
