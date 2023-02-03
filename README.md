RollbarLogger

Can send logs to Rollbar web service

To create a project:
    go here - https://rollbar.com/settings/accounts/flow.io/projects/?project_selector=1#create
    enter the service name and select Eastern as the timezone
    after clicking "create", select "Java" as the primary SDK
    it will provide you token witch we will need to use 
usage:

Need to create RollbarLogger("token") object with your token. U can set two properties. withSendToRollbar  boolean and withFrequency long

withFrequency:
    default value 1. it means that logger will send all logs (1/1)100% to rollbar.
    if you will set value 2 it will send only (1/2)50% logs.  3-(1/3)33% etc... 100 - only 1 percent of logs.
withSendToRollbar:
    default value true. it means that logger will send logs to rollbar and console.
    if you will set value false it will send logs only to console.

example:
    val rollbarLogger = RollbarLogger("a8d39c45f43f481398ab1b15f68a2eaf")
        .withFrequency(10)
        .withSendToRollbar(true)


to send logs we have to use methods below:
    rollbarLogger.info("info level message")
    rollbarLogger.error("error level message")
    rollbarLogger.warn("warning level message")
    rollbarLogger.debug("debug level message")

to add custom data to logs we have to use method addAttributes[T](key: String, value: T). 
Key should be string and value can be any data. Example:
    case class Attribute1(
        att: String
    )
    case class Attribute2(
        att: String,
        name: String
    )
    
    rollbarLogger.addAttributes("Z", Attribute1("ZZZ"))
    rollbarLogger.addAttributes("X", Attribute2("XXX", "YYY"))

Also we can set common attributes channelId, organization and requestId using methods below. Can use it with any data:
    rollbarLogger.channelId(Attribute1("channelId"))
    rollbarLogger.organization(Attribute2("1", "organization"))
    rollbarLogger.requestId(Attribute2("2", "requestId"))