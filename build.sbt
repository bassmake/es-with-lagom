name := "es-with-lagom"
version := "0.1"
scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  lagomScaladslServer
)

lazy val `api` = (project in file("api"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `impl` = (project in file("impl"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslPubSub,
    )
  )
  .dependsOn(
    `api`
  )

lazy val dependencies =
  new {
    // common stuff
    val scalaLoggingVersion = "3.9.0"

    // di
    val macwireVersion = "2.3.1"
    
    // test stuff
    val scalaTestVersion = "3.0.5"
    val pegdownVersion = "1.6.0"
    
    // dependencies
    val macwire = "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion

    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
    val pegdown = "org.pegdown" % "pegdown" % pegdownVersion


  }

val commonSettings =
//  commonSmlBuildSettings ++
//    wartRemoverSettings ++
//    ossPublishSettings ++
  smlBuildSettings ++ Seq(
//      wartremoverExcluded += baseDirectory.value / "src" / "test" / "scala",
    Test / fork := true,
    Test / logBuffered := false,
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-h", s"${baseDirectory.value}/target/test-html-report"),
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    )
  )
