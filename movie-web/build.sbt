
lazy val root = (project in file("."))
 .enablePlugins(PlayScala)
 .settings(
   name := """movie-web""",
   version := "2.6.x",
   scalaVersion := "2.12.15",
   libraryDependencies ++= Seq(
     guice,
     "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
     "net.debasishg" %% "redisclient" % "3.41",
     "org.apache.kafka" %% "kafka" % "3.0.0",
     "com.typesafe" % "config" % "1.4.1",
   ),

   scalacOptions ++= Seq(
     "-feature",
     "-deprecation",
     "-Xfatal-warnings"
   )

 )

