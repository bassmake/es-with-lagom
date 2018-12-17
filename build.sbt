name := "es-with-lagom"
version := "0.1"
scalaVersion := "2.12.7"

lazy val `root` = (project in file("."))
  .aggregate(
    `transaction-producer`,
    `api`,
    `impl`
  )

lazy val `transaction-producer-api` = (project in file("transaction-producer/api"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )


lazy val `transaction-producer-impl` = (project in file("transaction-producer/impl"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      lagomScaladslDevMode,
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      Dependencies.macwire
    )
  )
  .dependsOn(
    `transaction-producer-api`
  )

lazy val `transaction-producer` = (project in file("transaction-producer"))
  .aggregate(
    `transaction-producer-api`,
    `transaction-producer-impl`
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

val commonSettings =
//  commonSmlBuildSettings ++
//    wartRemoverSettings ++
//    ossPublishSettings ++
  smlBuildSettings ++ Seq(
//    wartremoverWarnings ++= Warts.allBut(Wart.Throw),
//      wartremoverExcluded += baseDirectory.value / "src" / "test" / "scala",
    Test / fork := true,
    Test / logBuffered := false,
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-h", s"${baseDirectory.value}/target/test-html-report"),
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    )
  )
