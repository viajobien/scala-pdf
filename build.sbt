releaseSettings

ReleaseKeys.crossBuild := true

name := "scala-pdf"

organization := "nl.rhinofly"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.10.4", scalaVersion.value)

libraryDependencies ++= Seq(
  "org.xhtmlrenderer" % "flying-saucer-core" % "9.0.7",
  "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.7",
  "net.sf.jtidy" % "jtidy" % "r938"
)

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

publishTo :=
  Some("Kaliber Repository" at "https://jars.kaliber.io/artifactory/libs-release-local")
