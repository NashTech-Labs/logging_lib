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

  val rollbarLogger = RollbarLogger("a8d39c45f43f481398ab1b15f68a2eaf")
    .withFrequency(10)
    .withSendToRollbar(true)

  rollbarLogger.addAttributes("Z", Attribute1("ZZZZ", "SSS1"))
  rollbarLogger.addAttributes("X", Attribute2("XXX", "SSS2"))
  rollbarLogger.addAttributes("C", Attribute3("CC", "SSS3"))

  rollbarLogger.organization(Attribute2("CC", "organization"))
  rollbarLogger.requestId(Attribute3("CC", "requestId"))

  rollbarLogger.info("info message")
  rollbarLogger.error("error message")
  rollbarLogger.warn("warning message")
  rollbarLogger.debug("debug message")
}
