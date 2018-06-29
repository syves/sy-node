resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

scalaVersion := "2.12.0"
libraryDependencies += "org.scalactic" %% "scalactic"           % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"
libraryDependencies += "org.spire-math" %% "kind-projector" % "0.9.7"
libraryDependencies += "co.upvest" %% "arweave4s-core" % "0.6.0"

name := "sy-node"
