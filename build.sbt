name := "moduload"
organization := "org.matthicks"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.13.3"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"