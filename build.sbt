ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion      := "2.13.10"
ThisBuild / fork              := true
ThisBuild / scalacOptions := optionsOnOrElse("2.13", "2.12")("-Ywarn-unused", "-Ymacro-annotations")("").value
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixDependencies ++= List("com.github.liancheng" %% "organize-imports" % "0.6.0")

def settingsApp = Seq(
  name := "zio-http-users",
  Compile / run / mainClass := Option("EndpointPatternApp"),
  libraryDependencies ++= Dependencies.globalProjectDependencies,
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(settingsApp)

addCommandAlias("fmt", "scalafmt; Test / scalafmt; sFix;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")
