ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Logging_lib"
  )

libraryDependencies += "com.rollbar" % "rollbar-java" % "1.9.0"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "6.3"