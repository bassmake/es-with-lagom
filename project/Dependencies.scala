import sbt._

object Dependencies {

  // common stuff
  val scalaLoggingVersion = "3.9.2"

  // di
  val macwireVersion = "2.3.1"

  // enumeration
  val enumeratumVersion = "1.5.14"

  // test stuff
  val scalaTestVersion = "3.0.5"
  val pegdownVersion = "1.6.0"

  // dependencies
  val macwire = "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  val enumeratum = "com.beachape" %% "enumeratum-play-json" % enumeratumVersion

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  val pegdown = "org.pegdown" % "pegdown" % pegdownVersion
  
}
