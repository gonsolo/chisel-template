ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "Gonsolo"

val chiselVersion = "5.1.0"

lazy val root = (project in file("."))
	.settings(
		name := "Pepe",
		libraryDependencies ++= Seq(
			"org.chipsalliance" %% "chisel" % chiselVersion,
			"edu.berkeley.cs" %% "chiseltest" % "5.0.2" % "test",

			// This has to be locally built and published via "sbt publishLocal"
			// from the berkeley-hardfloat repository
			// https://github.com/ucb-bar/berkeley-hardfloat
			"edu.berkeley.cs" %% "hardfloat" % "1.5-SNAPSHOT"
		),
		scalacOptions ++= Seq(
			"-language:reflectiveCalls",
			"-deprecation",
			"-feature",
			"-Xcheckinit",
			"-Ymacro-annotations",
		),
		addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full),
	)

