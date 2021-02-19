name := "moduload"
organization := "com.outr"
version := "1.1.1"

scalaVersion := "2.13.4"

crossScalaVersions := List("2.13.4", "2.12.12", "2.11.12", "3.0.0-M3")
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")

publishTo in ThisBuild := sonatypePublishTo.value
publishConfiguration in ThisBuild := publishConfiguration.value.withOverwrite(true)
sonatypeProfileName in ThisBuild := "com.outr"
publishMavenStyle in ThisBuild := true
licenses in ThisBuild := Seq("MIT" -> url("https://github.com/outr/moduload/blob/master/LICENSE"))
sonatypeProjectHosting in ThisBuild := Some(xerial.sbt.Sonatype.GitHubHosting("outr", "moduload", "matt@outr.com"))
homepage in ThisBuild := Some(url("https://github.com/outr/moduload"))
scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/outr/moduload"),
    "scm:git@github.com:outr/moduload.git"
  )
)
developers in ThisBuild := List(
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("http://matthicks.com"))
)

libraryDependencies ++= (if (isDotty.value) {
  Nil
} else {
  List("org.scala-lang.modules" %% "scala-collection-compat" % "2.4.2")
})
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % "test"