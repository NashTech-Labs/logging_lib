import com.rollbar.notifier.config.ConfigBuilder.withAccessToken;
import com.rollbar.notifier.Rollbar;

class LoggingRollbarUtil {
  val projectName = "projectName"
  val accessToken = "147eef2f5bfb4f3f8b7b6e1d353e0841"
  val version = "1.0.0"

  val rollbar: Rollbar = Rollbar.init(withAccessToken(accessToken)
    .environment(projectName)
    .codeVersion(version)
    .build());


  def info(info: String) = {
    rollbar.info(info)
  }

  def warning(warning: String) = {
    rollbar.info(warning)
  }

  def error(error: String) = {
    rollbar.error(error)
  }
}
