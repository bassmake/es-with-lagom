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
      dependencies.tagging,
      lagomLogback,
      lagomScaladslApi
    )
  )

lazy val dependencies =
  new {
    // common stuff
    val scalaLoggingVersion = "3.9.0"
    val taggingVersion = "2.2.1"
    val enumeratumVersion = "1.5.13"
    val enumeratumJsonVersion = "1.5.14"
    val chimneyVersion = "0.2.1"
    val derivedCodecsVersion = "4.0.1"
    val catsVersion = "1.3.1"
    val octopusVersion = "0.3.3"

    // event consumer
    val alpakkaJmsVersion = "1.0-M1"
    val activeMqVersion = "5.12.0" // this is version that we use on prod

    // di
    val macwireVersion = "2.3.1"

    // db
    val h2Version = "1.4.197"
    val oracleVersion = "11.2.0.4"
    val liquibaseVersion = "3.6.2"

    // api
    val rsqlVersion = "2.1.0"

    // test stuff
    val scalaTestVersion = "3.0.5"
    val pegdownVersion = "1.6.0"

    // dependencies
    val macwire = "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
    val tagging = "com.softwaremill.common" %% "tagging" % taggingVersion
    val chimney = "io.scalaland" %% "chimney" % chimneyVersion
    val cats = "org.typelevel" %% "cats-core" % catsVersion
    val octopus = "com.github.krzemin" %% "octopus-cats" % octopusVersion
    val enumeratum = "com.beachape" %% "enumeratum" % enumeratumVersion
    val enumeratumJson = "com.beachape" %% "enumeratum-play-json" % enumeratumJsonVersion
    val derivedCodecs = "org.julienrf" % "play-json-derived-codecs" % derivedCodecsVersion
    val alpakkaJms = "com.lightbend.akka" %% "akka-stream-alpakka-jms" % alpakkaJmsVersion
    val h2 = "com.h2database" % "h2" % h2Version
    val oracle = "com.oracle" % "ojdbc6" % oracleVersion
    val liquibase = "org.liquibase" % "liquibase-core" % liquibaseVersion
    val activeMqPool = "org.apache.activemq" % "activemq-pool" % activeMqVersion
    val activeMqAll = "org.apache.activemq" % "activemq-all" % activeMqVersion
    val rsql = "cz.jirutka.rsql" % "rsql-parser" % rsqlVersion

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
