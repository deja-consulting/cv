scalaVersion := "2.12.10"

organization := "consulting.deja"
name := "cv"
version := "1.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.japgolly.scalacss" %% "core" % "0.6.0-RC1",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

sourceGenerators in Compile += Def.task {
  GenerateTemplateSources(baseDirectory.value / "src" / "main", (sourceManaged in Compile).value)
}.taskValue

enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq(name, version, scalaVersion, sbtVersion)
buildInfoPackage := "consulting.deja.cv"
