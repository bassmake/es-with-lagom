import Dependencies._

name := "es-with-lagom"
version := "0.1"
scalaVersion := "2.12.8"

lazy val `root` = (project in file("."))
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomScaladslApi,
      lagomScaladslServer,
      lagomScaladslCluster,
      lagomScaladslDevMode,
      lagomScaladslPersistenceCassandra,
      scalaLogging,
      macwire,
      enumeratum,

      lagomScaladslTestKit,
      scalaTest % Test,
      pegdown % Test
    )
  )

val commonSettings =
//  commonSmlBuildSettings ++
//    wartRemoverSettings ++
//    ossPublishSettings ++
  smlBuildSettings ++ Seq(
//    wartremoverWarnings ++= Warts.allBut(Wart.Throw),
//    wartremoverExcluded += baseDirectory.value / "src" / "test" / "scala",
    Test / fork := true,
    Test / logBuffered := false,
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-h", s"${baseDirectory.value}/target/test-html-report"),
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    )
  )
