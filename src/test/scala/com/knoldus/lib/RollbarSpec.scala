package com.knoldus.lib

object RollbarSpec extends App {
  case class Attribute1(
    att: String,
    name: String
  )

  case class Attribute2(
    att: String,
    name: String
  )

  case class Attribute3(
    att: String,
    name: String
  )

  import play.api.libs.json._

  implicit val Attribute1Writes = Json.writes[Attribute1]
  implicit val Attribute1Format = Json.format[Attribute1]
  implicit val Attribute2Writes = Json.writes[Attribute2]
  implicit val Attribute2Format = Json.format[Attribute2]
  implicit val Attribute3Writes = Json.writes[Attribute3]
  implicit val Attribute3Format = Json.format[Attribute3]

  val rollbarLogger = RollbarProvider.logger("a8d39c45f43f481398ab1b15f68a2eaf").organization("organization")
    .withKeyValue("Z", Attribute1("ZZZZ", "SSS1"))
    .withKeyValue("X", Attribute2("XXX", "SSS2"))
    .withKeyValue("C", Attribute3("CC", "SSS3"))
    .requestId("requestId")

  rollbarLogger.info("info message!")
  rollbarLogger.error("error message!")
  rollbarLogger.warn("warning message!")
  rollbarLogger.debug("debug message!")

}
