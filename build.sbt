name := "inference-test"

version := "0.1"

scalaVersion in ThisBuild := "2.11.11"

lazy val root = (project in file(".")).aggregate(macros, core)

lazy val macros = (project in file("macros")).settings(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)

lazy val core = (project in file("core")).dependsOn(macros).settings(
  scalacOptions := Seq("-Xlog-implicits")
)