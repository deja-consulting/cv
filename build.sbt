scalaVersion := "3.8.4"

organization := "consulting.deja"
name := "cv"
version := "1.4.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.5.37",
  "com.github.japgolly.scalacss" %% "core" % "1.0.0",
  "org.scalatest" %% "scalatest" % "3.2.20" % Test
)

Compile / sourceGenerators += Def.task {
  GenerateTemplateSources(baseDirectory.value / "src" / "main", (Compile / sourceManaged).value)
}.taskValue

Compile / scalacOptions += "-Wconf:src=target/scala-3.8.4/src_managed/.*:silent"

enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq(name, version, scalaVersion, sbtVersion)
buildInfoPackage := "consulting.deja.cv"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-Wunused:all",
  "-Xkind-projector:underscores",
)
semanticdbEnabled := true
semanticdbVersion := scalafixSemanticdb.revision

addCommandAlias("localBuild", "scalafmtSbt;scalafixAll;scalafmtAll;compile;Test/compile")
