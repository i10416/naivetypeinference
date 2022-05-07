ThisBuild / scalaVersion := "3.1.2"

lazy val core = project
  .in(file("."))
  .settings(
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M3" % Test
  )
