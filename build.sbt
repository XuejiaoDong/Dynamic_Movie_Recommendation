name := "movie-recommendation"

version := "0.1"

scalaVersion := "2.12.15"

lazy val db_utils = project in file("db-utils")

lazy val movie_web = project in file("movie-web") dependsOn db_utils

lazy val movie_recommend = project in file("movie-recommend") dependsOn db_utils

lazy val dataflow_stimulator = project in file("dataflow-stimulator") dependsOn db_utils

lazy val ETL = project in file("ETL") dependsOn db_utils

lazy val root = (project in file(".")).aggregate(movie_web, movie_recommend, dataflow_stimulator, ETL, db_utils)

//parallelExecution in Test := false

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")
