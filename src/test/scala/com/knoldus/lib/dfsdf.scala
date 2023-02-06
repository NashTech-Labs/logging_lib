package com.knoldus.lib

import com.rollbar.notifier.Rollbar
import com.rollbar.notifier.config.ConfigBuilder.withAccessToken
object dfsdf extends App{
//val rollbar: Rollbar = Rollbar.init(withAccessToken("a8d39c45f43f481398ab1b15f68a2eaf")
//  .environment("qa")
//  .codeVersion("1.0.0")
//  .build());
//val rollbarLogger =
val rollbarLogger1 =RollbarProvider.logger("a8d39c45f43f481398ab1b15f68a2eaf", Map.empty)

 // rollbarLogger.info("123123")


 // val rollbarLogger1 =RollbarLogger(Map.empty, "a8d39c45f43f481398ab1b15f68a2eaf")

  rollbarLogger1.info("123123")
}
