//@see http://www.scala-sbt.org/release/docs/Detailed-Topics/Publishing

publishMavenStyle := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

