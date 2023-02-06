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
  val rollbarLogger =
    RollbarProvider.logger("a8d39c45f43f481398ab1b15f68a2eaf", Map.empty).withFrequency(1)
      .withSendToRollbar(true)

  import play.api.libs.json._

  //val rollbarLogger1 =RollbarLogger(Map.empty, "a8d39c45f43f481398ab1b15f68a2eaf")

  implicit val residentWrites = Json.writes[Attribute1]
  import play.api.libs.json._

  implicit val residentFormat = Json.format[Attribute1]
  //rollbarLogger.withKeyValue("Z", Attribute1("ZZZZ", "SSS1"))
//rollbarLogger.withKeyValue("X", Attribute2("XXX", "SSS2"))
//rollbarLogger.withKeyValue("C", Attribute3("CC", "SSS3"))

  //rollbarLogger.organization(/*Attribute2("CC", "organization")*/"")
  /////rollbarLogger.requestId(/*Attribute3("CC", "requestId")*/"")

 // rollbarLogger1.info("infoxcvxc message11")
  //rollbarLogger.error("error message")
  //rollbarLogger.warn("warning message")
  //rollbarLogger.debug("debug message")


  println("!21")
}
