name := "learning-Concurrent-Programming-Scala"

version := "1.0"

scalaVersion := "2.12.5"

fork := false

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.19.1"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-swing
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.7"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-remote
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.17"
