import RollbarLogger.Keys
import com.rollbar.notifier.config.ConfigBuilder.withAccessToken
import com.rollbar.notifier.Rollbar
import net.logstash.logback.marker.Markers.appendEntries
import org.slf4j.LoggerFactory
import scala.util.Random
import java.util.HashMap

case class RollbarLogger(
  rollbarToken: String,
  frequency: Long = 1L,
  shouldSendToRollbar: Boolean = true
)
{
  private val logger = LoggerFactory.getLogger("application")

  private var attributes: java.util.Map[String, Object] = new HashMap[String,Object]()

  val rollbar: Rollbar = Rollbar.init(withAccessToken(rollbarToken)
    .environment("qa")
    .codeVersion("1.0.0")
    .build());

  def addAttributes[T](key: String, value: T) = {
    attributes.put(key, value.asInstanceOf[Object])
  }

  def withFrequency(frequency: Long): RollbarLogger = this.copy(frequency = frequency)
  def withSendToRollbar(sendToRollbar: Boolean): RollbarLogger = this.copy(shouldSendToRollbar = sendToRollbar)
  def requestId[T](value: T) = addAttributes(Keys.RequestId, value)
  def channelId[T](value: T) = addAttributes(Keys.Channel_id, value)
  def organization[T](value: T) = addAttributes(Keys.Organization, value)

  def debug(message: => String): Unit = debug(message, null)

  def info(message: => String): Unit = info(message, null)

  def warn(message: => String): Unit = warning(message, null)

  def error(message: => String): Unit = error(message, null)

  def error(message: => String, error: => Throwable): Unit = {
    if (shouldLog) {
      logger.error(appendEntries(attributes), message, error)
      if (shouldSendToRollbar) rollbar.error(error, attributes, message)
    }
  }

  def info(message: => String, error: => Throwable): Unit = {
    if (shouldLog) {
      logger.info(appendEntries(attributes), message, error)
      if (shouldSendToRollbar) {
        rollbar.info(error, attributes, message)
        rollbar.close(true)
      }
    }
  }

 def debug(message: => String, error: => Throwable): Unit = {
   if (shouldLog) {
     logger.debug(appendEntries(attributes), message, error)
     if (shouldSendToRollbar) rollbar.debug(error, attributes, message)
   }
 }

 def warning(message: => String, error: => Throwable): Unit = {
   if (shouldLog) {
     logger.warn(appendEntries(attributes), message, error)
     if (shouldSendToRollbar) rollbar.warning(error, attributes, message)
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
  }
}