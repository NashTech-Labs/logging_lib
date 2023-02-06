package com.knoldus.lib

import com.rollbar.notifier.Rollbar
import net.logstash.logback.marker.Markers.appendEntries
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsValue, Json, Writes}

import scala.jdk.CollectionConverters._
import scala.util.Random

case class RollbarLogger(
  rollbar: Rollbar,
  attributes: Map[String, JsValue] = Map.empty[String, JsValue],
  rollbarToken: String,
  frequency: Long = 1L,
  shouldSendToRollbar: Boolean = true
)
{
  private val logger = LoggerFactory.getLogger("application")

  import RollbarLogger._

  def withKeyValue[T: Writes](keyValue: (String, T)): RollbarLogger = withKeyValue(keyValue._1, keyValue._2)
  def withKeyValues[T: Writes](keyValue: (String, Seq[T])): RollbarLogger = withKeyValues(keyValue._1, keyValue._2)

  def withKeyValue[T: Writes](key: String, value: T): RollbarLogger = this.copy(attributes = attributes + (key -> Json.toJson(value)))
  def withFrequency(frequency: Long): RollbarLogger = this.copy(frequency = frequency)
  def withSendToRollbar(sendToRollbar: Boolean): RollbarLogger = this.copy(shouldSendToRollbar = sendToRollbar)
  def requestId(value: String): RollbarLogger = withKeyValue(Keys.RequestId, value)
  def organization(value: String): RollbarLogger = withKeyValue(Keys.Organization, value)
  def debug(message: => String): Unit = debug(message, null)

  def info(message: => String): Unit = info(message, null)

  def warn(message: => String): Unit = warning(message, null)

  def error(message: => String): Unit = error(message, null)

  def error(message: => String, error: => Throwable): Unit = {
    if (shouldLog) {
      logger.error(appendEntries(convert(attributes)), message, error)
      if (shouldSendToRollbar) {
        rollbar.error(error, convert(attributes), message)
        rollbar.close(true)
      }
    }
  }

  def info(message: => String, error: => Throwable): Unit = {
    if (shouldLog) {
      logger.info(appendEntries(convert(attributes)), message, error)
      if (shouldSendToRollbar) {
        rollbar.info(error, convert(attributes), message)
        rollbar.close(true)
      }
    }
  }

 def debug(message: => String, error: => Throwable): Unit = {
   if (shouldLog) {
     logger.debug(appendEntries(convert(attributes)), message, error)
     if (shouldSendToRollbar) {
       rollbar.debug(error, convert(attributes), message)
       rollbar.close(true)
     }
   }
 }

 def warning(message: => String, error: => Throwable): Unit = {
   if (shouldLog) {
     logger.warn(appendEntries(convert(attributes)), message, error)
     if (shouldSendToRollbar) {
       rollbar.warning(error, convert(attributes), message)
       rollbar.close(true)
     }
   }
 }

  private def shouldLog: Boolean =
    frequency == 1L || (Random.nextInt() % frequency == 0)
}

object RollbarLogger {
  object Keys {
    val RequestId = "request_id"
    val Organization = "organization"
    val Channel_id = "channel_id"
    val Fingerprint = "fingerprint"
  }

  def convert(attributes: Map[String, JsValue]): java.util.Map[String, Object] =
    attributes.asJava.asInstanceOf[java.util.Map[String, Object]]
}