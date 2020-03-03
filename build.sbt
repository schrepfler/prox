name := "prox"
description := "A Scala library for working with system processes"
organization := "io.github.vigoo"

dynverSonatypeSnapshots in ThisBuild := true

val scala212 = "2.12.10"
val scala213 = "2.13.1"

scalaVersion := scala213
crossScalaVersions := List(scala212, scala213)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.1.0",
  "co.fs2" %% "fs2-core" % "2.2.2",
  "co.fs2" %% "fs2-io" % "2.2.2",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.4",

  "dev.zio" %% "zio" % "1.0.0-RC17" % "test",
  "dev.zio" %% "zio-test"     % "1.0.0-RC17" % "test",
  "dev.zio" %% "zio-test-sbt" % "1.0.0-RC17" % "test",
  "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC10" % "test",
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

coverageEnabled in(Test, compile) := true
coverageEnabled in(Compile, compile) := false

val scalacOptions212 = Seq("-Ypartial-unification", "-deprecation")
val scalacOptions213 = Seq("-deprecation")

scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 12)) => scalacOptions212
  case Some((2, 13)) => scalacOptions213
  case _ => Nil
})

// Publishing

publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

publishTo := sonatypePublishTo.value

import microsites.ConfigYml
import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("vigoo", "prox", "daniel.vigovszky@gmail.com"))

developers := List(
  Developer(id="vigoo", name="Daniel Vigovszky", email="daniel.vigovszky@gmail.com", url=url("https://vigoo.github.io"))
)

credentials ++=
  (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield
    Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org",
      username,
      password)).toSeq

usePgpKeyHex("2EE41B349B2FEC81194CEB226B4C91F63271AE2B")

enablePlugins(GhpagesPlugin)
enablePlugins(SiteScaladocPlugin)
enablePlugins(MicrositesPlugin)

git.remoteRepo := "git@github.com:vigoo/prox.git"

micrositeUrl := "https://vigoo.github.io"
micrositeBaseUrl := "/prox"
micrositeHomepage := "https://vigoo.github.io/prox/"
micrositeDocumentationUrl := "/prox/docs"
micrositeAuthor := "Daniel Vigovszky"
micrositeTwitterCreator := "@dvigovszky"
micrositeGithubOwner := "vigoo"
micrositeGithubRepo := "prox"
micrositeGitterChannel := false
micrositeDataDirectory := file("src/microsite/data")
micrositeStaticDirectory := file("src/microsite/static")
micrositeImgDirectory := file("src/microsite/img")
micrositeCssDirectory := file("src/microsite/styles")
micrositeSassDirectory := file("src/microsite/partials")
micrositeJsDirectory := file("src/microsite/scripts")
micrositeTheme := "light"
micrositeHighlightLanguages ++= Seq("scala", "sbt")
micrositeConfigYaml := ConfigYml(
  yamlCustomProperties = Map("plugins" -> List("jemoji"))
)