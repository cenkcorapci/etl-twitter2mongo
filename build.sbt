name := "etl-twitter2mongo"

version := "0.0.1"

scalaVersion := "2.12.4"

scalacOptions := Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

libraryDependencies ++= {
  val akkaHttpV = "10.0.11"
  val scalaLoggingV = "3.7.2"
  val betterFilesV = "3.4.0"
  val casbahV = "3.1.1"
  val salatV = "1.11.2"
  val logbackV = "1.2.3"
  val logbackEncoderV = "4.11"

  Seq(
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "org.mongodb" %% "casbah" % casbahV,
    "com.github.salat" %% "salat" % salatV,
    "com.github.pathikrit" %% "better-files" % betterFilesV,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV,
    "ch.qos.logback" % "logback-core" % logbackV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "net.logstash.logback" % "logstash-logback-encoder" % logbackEncoderV
  )
}

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.filterDistinctLines
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// define setting key to write configuration to .scalafmt.conf
SettingKey[Unit]("scalafmtGenerateConfig") :=
  IO.write( // writes to file once when build is loaded
    file(".scalafmt.conf"),
    """style = IntelliJ
      |# Your configuration here
    """.stripMargin.getBytes("UTF-8")
  )