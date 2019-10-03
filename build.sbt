import Dependencies._

name := "es-with-lagom"
version := "0.1"
scalaVersion := "2.12.10"

scalafmtOnCompile in ThisBuild := true

lazy val `es-with-lagom` = (project in file("."))
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      compilerPlugin("com.softwaremill.neme" %% "neme-plugin" % "0.0.4"),
      lagomLogback,
      lagomScaladslApi,
      lagomScaladslServer,
      lagomScaladslDevMode,
      lagomScaladslKafkaClient,
      lagomScaladslPersistenceCassandra,
      scalaLogging,
      macwire,
      enumeratum,

      lagomScaladslTestKit,
      lagomScaladslKafkaBroker % Test,
      scalaTest % Test,
      pegdown % Test
    )
  )

val commonSettings =
  commonSmlBuildSettings ++
    splainSettings ++
    dependencyUpdatesSettings ++
    acyclicSettings ++
    Seq(
//    wartremoverWarnings ++= Warts.allBut(Wart.Throw),
//    wartremoverExcluded += baseDirectory.value / "src" / "test" / "scala",
    Test / fork := true,
    Test / logBuffered := false,
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-h", s"${baseDirectory.value}/target/test-html-report"),
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    )
  )
