name := "moduload"
organization := "com.outr"
version := "1.1.7"

scalaVersion := "2.13.11"

crossScalaVersions := List(
  "2.13.11",
  
  "2.12.18",
  
  "2.11.12",
  
  "3.3.0"
)
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / publishTo := sonatypePublishTo.value
ThisBuild / sonatypeProfileName := "com.outr"
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
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("https://matthicks.com"))
)

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.11.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test