lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "bla",
//    libraryDependencies += scalaTest % Test
      // https://mvnrepository.com/artifact/com.google.guava/guava

)
libraryDependencies += "com.google.guava" % "guava" % "24.1-jre"
// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

