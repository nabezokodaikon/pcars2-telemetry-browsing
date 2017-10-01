lazy val commonSettings = Seq(
  organization := "com.github.nabezokodaikon",
  version := "0.0.1",
  scalaVersion := "2.12.3",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked"
  ),
  scalacOptions in (Compile, console) -= "-Ywarn-unused-import"
)

lazy val root = (project.in(file(".")))
  .settings(commonSettings: _*)
  .settings(
    name := "pcars2-udp-app",
    resolvers ++= {
      Seq(
      )
    },
    libraryDependencies ++= {
      Seq(
        // Test
        "org.scalactic" %% "scalactic" % "3.0.1",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",
        "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",

        // Logger
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",

        // "com.typesafe.akka" %% "akka-actor" % "2.5.4",
        // "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test,
        "com.typesafe.akka" %% "akka-actor" % "2.4.19",
        "com.typesafe.akka" %% "akka-testkit" % "2.4.19" % Test,

        "com.typesafe.akka" %% "akka-stream" % "2.4.19",
        "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.19" % Test,

        "com.typesafe.akka" %% "akka-http" % "10.0.10",
        "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
        "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"
      )
    }
  )

initialCommands := "import com.github.nabezokodaikon._"
