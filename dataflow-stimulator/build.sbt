name := "dataflow-stimulator"

version := "0.1"

scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "3.0.0",
  "com.typesafe" % "config" % "1.4.1",

  "org.scalactic" %% "scalactic" % "3.2.10",
  "org.scalatest" %% "scalatest" % "3.2.9" % "test",

  "org.slf4j" % "slf4j-api" % "1.7.32",
  "org.slf4j" % "slf4j-simple" % "1.7.32",
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "mysql" % "mysql-connector-java" % "5.1.24",
)