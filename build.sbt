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

// https://mvnrepository.com/artifact/org.mockito/mockito-core
libraryDependencies += "org.mockito" % "mockito-core" % "2.23.0" % Test

// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.3.1" % Test
// https://mvnrepository.com/artifact/org.assertj/assertj-core
libraryDependencies += "org.assertj" % "assertj-core" % "3.11.1" % Test


