name := "code"

version := "0.1"

scalaVersion := "2.12.6"


scalacOptions += "-Ypartial-unification"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.2.0"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
