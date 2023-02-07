package com.knoldus.lib

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{ObjectMapper, SerializerProvider}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.rollbar.api.payload.Payload
import com.rollbar.api.payload.data.Data
import com.rollbar.notifier.Rollbar
import com.rollbar.notifier.config.ConfigBuilder
import com.rollbar.notifier.fingerprint.FingerprintGenerator
import com.rollbar.notifier.sender.json.JsonSerializer
import com.rollbar.notifier.sender.result.Result
import play.api.libs.json.{JsError, JsObject, JsSuccess, Json, JsonParserSettings}
import play.api.libs.json.jackson.PlayJsonModule

object RollbarProvider {
  def logger(
    token: String = ""
  ): RollbarLogger = {
    RollbarLogger(rollbar(token), Map.empty, token)
  }

  private def rollbar(token: String): Rollbar = {
    val baseConfig = RollbarProvider.baseConfig(token)
    Rollbar.init(baseConfig)
  }

  private def baseConfig(token: String): com.rollbar.notifier.config.Config = {
    val fingerprintGenerator = new FingerprintGenerator {
      override def from(data: Data): String = {
        Option(data.getCustom)
          .flatMap(custom => Option(custom.get(RollbarLogger.Keys.Fingerprint)))
          .map(_.toString)
          .orNull
      }
    }

    val jacksonSerializer: JsonSerializer = new com.rollbar.notifier.sender.json.JsonSerializer {

      val mapper = new ObjectMapper()

      mapper.registerModule(new PlayJsonModule(JsonParserSettings()))

      mapper.registerModule(new SimpleModule() {
        addSerializer(
          classOf[com.rollbar.api.json.JsonSerializable],
          (value: com.rollbar.api.json.JsonSerializable, gen: JsonGenerator, serializers: SerializerProvider) => {
            serializers.defaultSerializeValue(value.asJson(), gen)
          }
        )
      })

      override def toJson(payload: Payload): String = {
        mapper.writeValueAsString(payload)
      }

      override def resultFrom(response: String): Result = {
        val resultOpt = for {
          obj <- Json.parse(response).validate[JsObject]
          err <- (obj \ "err").validate[Int]
          content <- {
            if (err == 0)
              (obj \ "result" \ "uuid").validate[String]
            else
              (obj \ "message").validate[String]
          }
        } yield new Result.Builder().code(err).body(content).build

        resultOpt match {
          case JsSuccess(res, _) => res
          case JsError(errors) => new Result.Builder().code(-1).body(
            JsError.toJson(errors).toString
          ).build
        }
      }
    }

    ConfigBuilder
      .withAccessToken(token)
      .handleUncaughtErrors(true)
      .language("scala")
      .fingerPrintGenerator(fingerprintGenerator)
      .jsonSerializer(jacksonSerializer)
      .codeVersion(sys.env.getOrElse("DD_VERSION", null))
      .build()
  }
}
