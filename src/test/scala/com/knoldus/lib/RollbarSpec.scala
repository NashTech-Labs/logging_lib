package com.knoldus.lib

object RollbarSpec extends App {
  case class Attribute(
    attName: String,
    value: String
  )

  import play.api.libs.json._

  implicit val AttributeWrites: OWrites[Attribute] = Json.writes[Attribute]
  implicit val AttributeFormat: OFormat[Attribute] = Json.format[Attribute]

  val rollbarLogger = RollbarProvider
    .logger("TOKEN")
    .organization("organization1")
    .requestId("requestId1")
    .withKeyValue("applicationName1", Attribute("appName", "Knoldus test log lib"))
    .withKeyValue("region1", "Canada")

  rollbarLogger.info("info message again!")
  rollbarLogger.error("error message again!")
  rollbarLogger.warn("warning message again!")
  rollbarLogger.debug("debug message again!")

}
