name := "kafka-streams-scala"

organization := "com.github.aseigneurin"

crossScalaVersions in ThisBuild := Seq("2.11.11", "2.12.3")

scalacOptions := Seq("-Xexperimental", "-unchecked", "-deprecation")

val versions = new {
  val kafka   = "0.11.0.1"
  val jackson = "2.8.8"
  val slf4j   = "1.7.25"
  val logging = "3.5.0"
}

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-streams" % versions.kafka,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % versions.jackson,
  "org.slf4j" % "slf4j-log4j12" % versions.slf4j,
  "com.typesafe.scala-logging" %% "scala-logging" % versions.logging,
)
