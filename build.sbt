name := "moduload"
organization := "com.outr"
version := "1.1.5"

scalaVersion := "2.13.5"

crossScalaVersions := List("2.13.5", "2.12.13", "2.11.12", "3.0.1")
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

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"
libraryDependencies += "com.outr" %% "testy" % "1.0.6" % Test

testFrameworks += new TestFramework("munit.Framework")