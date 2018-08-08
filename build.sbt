import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._
import ReleaseTransformations._

name := "scala-pdf"
organization := "com.viajobien"
scalaVersion := "2.12.6"
crossScalaVersions := Seq("2.10.7", "2.11.12", scalaVersion.value)

lazy val root = project in file(".")

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
org.scalastyle.sbt.ScalastylePlugin.autoImport.scalastyleFailOnError := true
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.autoImport.scalastyle.in(Compile).toTask("").value
compileScalastyle in Compile := (compileScalastyle in Compile).dependsOn(SbtScalariform.autoImport.scalariformFormat in Compile).value
compile in Compile := (compile in Compile).dependsOn(compileScalastyle in Compile).value

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(SpacesAroundMultiImports, true)

coverageMinimum := 90
coverageFailOnMinimum := true

libraryDependencies ++= {
  val flyingSaucerVersion = "9.0.8"
  Seq(
    "org.xhtmlrenderer" % "flying-saucer-core" % flyingSaucerVersion,
    "org.xhtmlrenderer" % "flying-saucer-pdf" % flyingSaucerVersion,
    "net.sf.jtidy" % "jtidy" % "r938",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
}

releaseCrossBuild := true
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // For non cross-build projects, use releaseStepCommand("publishSigned")
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

credentials += Credentials(Path.userHome / ".ivy2" / ".vb_sonatype")
sonatypeProfileName := organization.value
publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

homepage := Some(url("https://github.com/viajobien/scala-pdf"))
licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php"))
pomExtra := {
  <scm>
    <url>git@github.com:viajobien/scala-pdf.git</url>
    <connection>scm:git@github.com:viajobien/scala-pdf.git</connection>
  </scm>
}
