name := "moduload"
organization := "com.outr"
version := "1.1.3-SNAPSHOT"

scalaVersion := "2.13.5"

crossScalaVersions := List("2.13.5", "2.12.13", "2.11.12", "3.0.0-RC1", "3.0.0-RC2")
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

ThisBuild / publishTo := sonatypePublishTo.value
ThisBuild / publishConfiguration := publishConfiguration.value.withOverwrite(true)
ThisBuild / sonatypeProfileName := "com.outr"
ThisBuild / publishMavenStyle := true
ThisBuild / licenses := Seq("MIT" -> url("https://github.com/outr/moduload/blob/master/LICENSE"))
ThisBuild / sonatypeProjectHosting := Some(xerial.sbt.Sonatype.GitHubHosting("outr", "moduload", "matt@outr.com"))
ThisBuild / homepage := Some(url("https://github.com/outr/moduload"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/outr/moduload"),
    "scm:git@github.com:outr/moduload.git"
  )
)
ThisBuild / developers := List(
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("http://matthicks.com"))
)

libraryDependencies ++= (if (isDotty.value) {
  Nil
} else {
  List("org.scala-lang.modules" %% "scala-collection-compat" % "2.4.3")
})
libraryDependencies += "com.outr" %% "testy" % "1.0.3" % Test

testFrameworks += new TestFramework("munit.Framework")