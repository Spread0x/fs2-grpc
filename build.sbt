import Dependencies._

inThisBuild(
  List(
    scalaVersion := "2.13.1",
    organization := "org.lyranthe.fs2-grpc",
    git.useGitDescribe := true,
    scmInfo := Some(ScmInfo(url("https://github.com/fiadliel/fs2-grpc"), "git@github.com:fiadliel/fs2-grpc.git"))
  ))

lazy val root = project.in(file("."))
  .enablePlugins(GitVersioning, BuildInfoPlugin)
  .settings(
    sonatypeProfileName := "org.lyranthe",
    skip in publish := true,
    pomExtra in Global := {
      <url>https://github.com/fiadliel/fs2-grpc</url>
        <licenses>
          <license>
            <name>MIT</name>
              <url>https://github.com/fiadliel/fs2-grpc/blob/master/LICENSE</url>
          </license>
        </licenses>
        <developers>
          <developer>
            <id>fiadliel</id>
            <name>Gary Coady</name>
            <url>https://www.lyranthe.org/</url>
          </developer>
        </developers>
    }
  )
  .aggregate(`sbt-java-gen`, `java-runtime`)

lazy val `sbt-java-gen` = project
  .enablePlugins(GitVersioning, BuildInfoPlugin)
  .settings(
    scalaVersion := "2.12.10",
    publishTo := sonatypePublishToBundle.value,
    sbtPlugin := true,
    crossSbtVersions := List(sbtVersion.value),
    buildInfoPackage := "org.lyranthe.fs2_grpc.buildinfo",
    addSbtPlugin(sbtProtoc),
    libraryDependencies += scalaPbCompiler
  )

lazy val `java-runtime` = project
  .enablePlugins(GitVersioning)
  .settings(
    scalaVersion := "2.13.1",
    crossScalaVersions := List(scalaVersion.value, "2.12.10"),
    publishTo := sonatypePublishToBundle.value,
    libraryDependencies ++= List(fs2, catsEffect, grpcCore) ++ List(grpcNetty, catsEffectLaws, minitest).map(_  % Test),
    mimaPreviousArtifacts := Set(organization.value %% name.value % "0.3.0"),
    Test / parallelExecution := false,
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    addCompilerPlugin(kindProjector)
  )
