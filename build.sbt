scalariformSettings

val flywayPlayVersion = "2.1.0-SNAPSHOT"

val scalatest = "org.scalatest" %% "scalatest" % "2.2.+" % "test"

lazy val plugin = Project (
  id = "plugin",
  base = file ("plugin")
).settings(
  Seq(
    name := "flyway-play",
    organization := "org.flywaydb",
    version := flywayPlayVersion,
    scalaVersion := "2.11.7",
    crossScalaVersions := scalaVersion.value :: "2.10.5" :: Nil,
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % play.core.PlayVersion.current % "provided",
      "com.typesafe.play" %% "play-test" % play.core.PlayVersion.current % "test"
        excludeAll ExclusionRule(organization = "org.specs2"),
      "org.flywaydb" % "flyway-core" % "3.2.+",
      scalatest
    ),
    scalacOptions ++= Seq("-language:_", "-deprecation")
  ) ++ scalariformSettings ++ publishingSettings :_*
)

val appDependencies = Seq(
  "com.h2database" % "h2" % "[1.4,)",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.play" %% "play-test" % play.core.PlayVersion.current % "test"
    excludeAll ExclusionRule(organization = "org.specs2"),
  "org.scalikejdbc" %% "scalikejdbc"                  % "2.2.+",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.4.+",
  scalatest
)

val playAppName = "playapp"
val playAppVersion = "1.0-SNAPSHOT"

lazy val playapp = Project(
  playAppName,
  file("playapp")
).enablePlugins(play.sbt.PlayScala).settings(scalariformSettings:_*)
.settings(
  resourceDirectories in Test += baseDirectory.value / "conf",
  scalaVersion := "2.11.7",
  version := playAppVersion,
  libraryDependencies ++= appDependencies
)
.dependsOn(plugin)
.aggregate(plugin)

val publishingSettings = Seq(
  publishMavenStyle := true,
  publishTo <<= version { (v: String) => _publishTo(v) },
  publishArtifact in Test := false,
  pomExtra := _pomExtra
)

def _publishTo(v: String) = {
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

val _pomExtra =
  <url>https://github.com/flyway/flyway-play</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://github.com/flyway/flyway-play/blob/master/LICENSE.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:flyway/flyway-play.git</url>
    <connection>scm:git:git@github.com:flyway/flyway-play.git</connection>
  </scm>
  <developers>
    <developer>
      <id>tototoshi</id>
      <name>Toshiyuki Takahashi</name>
      <url>http://tototoshi.github.com</url>
    </developer>
  </developers>
