import RollbarLogger.{Keys, convert}
import com.rollbar.notifier.config.ConfigBuilder.withAccessToken
import com.rollbar.notifier.Rollbar
import net.logstash.logback.marker.Markers.appendEntries
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters.IterableHasAsJava;

case class RollbarLogger[T](
  rollbarToken: String,
  projectName: String,
  attributes: Map[String, T],
  shouldSendToRollbar: Boolean = true
)
{
  private val logger = LoggerFactory.getLogger("application")

  val rollbar: Rollbar = Rollbar.init(withAccessToken(rollbarToken)
    .environment(projectName)
    .codeVersion("1.0.0")
    .build());

  def withKeyValue(key: String, value: T): RollbarLogger[T] = this.copy(attributes = attributes + (key -> value))
  def withSendToRollbar(sendToRollbar: Boolean): RollbarLogger[T] = this.copy(shouldSendToRollbar = sendToRollbar)
  def requestId(value: T): RollbarLogger[T] = withKeyValue(Keys.RequestId, value)
  def fingerprint(value: T): RollbarLogger[T] = withKeyValue(Keys.Fingerprint, value)
  def organization(value: T): RollbarLogger[T] = withKeyValue(Keys.Organization, value)

  def error(message: => String, error: => Throwable): Unit = {
    logger.error(appendEntries(convert(attributes)), message, error)
    if (shouldSendToRollbar) rollbar.error(error, convert(attributes), message)
  }

  def info(message: => String, error: => Throwable): Unit = {
    logger.info(appendEntries(convert(attributes)), message, error)
    if (shouldSendToRollbar) rollbar.info(error, convert(attributes), message)
  }

  def debug(message: => String, error: => Throwable): Unit = {
    logger.debug(appendEntries(convert(attributes)), message, error)
    if (shouldSendToRollbar) rollbar.debug(error, convert(attributes), message)
  }

  def warning(message: => String, error: => Throwable): Unit = {
    logger.warn(appendEntries(convert(attributes)), message, error)
    if (shouldSendToRollbar) rollbar.warning(error, convert(attributes), message)
  }
}

object RollbarLogger {
  object Keys {
    val RequestId = "request_id"
    val Organization = "organization"
    val Fingerprint = "fingerprint"
  }

  def convert[T](attributes: Map[String, T]): java.util.Map[String, Object] =
    attributes.asJava.asInstanceOf[java.util.Map[String, Object]]
}