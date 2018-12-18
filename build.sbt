name := "es-with-lagom"
version := "0.1"
scalaVersion := "2.12.7"

lazy val `root` = (project in file("."))
  .aggregate(
    `transaction-producer`,
    `customer-transactions`
  )

lazy val `transaction-producer` = (project in file("transaction-producer"))
  .aggregate(
    `transaction-producer-api`,
    `transaction-producer-impl`
  )

lazy val `transaction-producer-api` = (project in file("transaction-producer/api"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomScaladslApi
    )
  )

lazy val `transaction-producer-impl` = (project in file("transaction-producer/impl"))
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomScaladslServer,
      lagomScaladslDevMode,
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      Dependencies.macwire,

      lagomScaladslTestKit,
      Dependencies.scalaTest % Test,
      Dependencies.pegdown % Test
    )
  )
  .dependsOn(
    `transaction-producer-api`
  )

lazy val `customer-transactions` = (project in file("customer-transactions"))
  .aggregate(
    `customer-transactions-api`,
    `customer-transactions-impl`
  )

lazy val `customer-transactions-api` = (project in file("customer-transactions/api"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomScaladslApi
    )
  )

lazy val `customer-transactions-impl` = (project in file("customer-transactions/impl"))
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomScaladslServer,
      lagomScaladslDevMode,
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaClient,
      Dependencies.macwire,

      lagomScaladslTestKit,
      Dependencies.scalaTest % Test,
      Dependencies.pegdown % Test
    )
  )
  .dependsOn(
    `transaction-producer-api`
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
