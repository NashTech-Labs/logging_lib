### com.knoldus.lib.RollbarLogger

Can send logs to Rollbar web service

### To create account in Rollbar:
    go here - https://rollbar.com/login/ and create a account
    create a sample project and enter the service name and select Eastern as the timezone
    after clicking "create", select "Java" as the primary SDK
    it will provide you token which is needed send logs to rollbar 

### usage:

Add the following library dependency in your `build.sbt` file. This will import all the knoldus logging lib in your project.
##### sbt
```
resolvers += "google-artifact-registry" at "https://asia-maven.pkg.dev/sonarqube-289802/knoldus-aws-lib/"

libraryDependencies += "knoldus" % "logging_lib_2.13" % "1.0"
```

##### Maven
```
<dependency>
    <groupId>knoldus</groupId>
    <artifactId>logging_lib_2.13</artifactId>
    <version>1.0</version>
</dependency>
```

##### Gradle
```
compile group: 'knoldus', name: 'logging_lib_2.13', version: '1.0'
```


Need to create com.knoldus.lib.RollbarLogger("token") object with your token. U can set two properties. withSendToRollbar  boolean and withFrequency long

#### withFrequency:
    default value 1. it means that logger will send all logs (1/1)100% to rollbar.
    if you will set value 2 it will send only (1/2)50% logs.  3-(1/3)33% etc... 100 - only 1 percent of logs.
#### withSendToRollbar:
    default value true. it means that logger will send logs to rollbar and console.
    if you will set value false it will send logs only to console.

### How to use
To create rollbar logger instance -
``` 
val rollbarLogger = 
com.knoldus.lib.RollbarLogger("Rollbar_Token")
        .withFrequency(10)   // by default 1
        .withSendToRollbar(true)  // by default true
```

to send logs we have to use methods below:
```
    rollbarLogger.info("info level message")
    rollbarLogger.error("error level message")
    rollbarLogger.warn("warning level message")
    rollbarLogger.debug("debug level message")
```

to add custom data to logs we have to use method withKeyValue[T](key: String, value: T). 
Key should be string and value can be any data. Example:
```
    case class Attribute1(
        att: String
    )
    case class Attribute2(
        att: String,
        name: String
    )
    
rollbarLogger.withKeyValue("applicationName1", Attribute("appName", "Knoldus test log lib"))
rollbarLogger.withKeyValue("region1", "Canada")
```
 
Also we can set common attributes fingerprint, organization and requestId using methods below. Can use it with any data:
```
    rollbarLogger.fingerprint(Attribute1("fingerprint"))
    rollbarLogger.organization(Attribute2("1", "organization"))
    rollbarLogger.requestId(Attribute2("2", "requestId"))
```

#### Sample Code to Push Logs to RollBar

```
  case class Attribute(
    attName: String,
    value: String
  )

  import play.api.libs.json._

  implicit val AttributeWrites: OWrites[Attribute] = Json.writes[Attribute]
  implicit val AttributeFormat: OFormat[Attribute] = Json.format[Attribute]

  val rollbarLogger = RollbarProvider
    .logger("216743d7c9394c26bd89fc83983d15bd")
    .organization("organization1")
    .requestId("requestId1")
    .withKeyValue("applicationName1", Attribute("appName", "Knoldus test log lib"))
    .withKeyValue("region1", "Canada")

  rollbarLogger.info("info message again!")
  rollbarLogger.error("error message again!")
  rollbarLogger.warn("warning message again!")
  rollbarLogger.debug("debug message again!")
```

##### Sample POM.XML

```
<?xml version='1.0' encoding='UTF-8'?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>knoldus</groupId>
    <artifactId>logging_lib_2.13</artifactId>
    <packaging>jar</packaging>
    <description>Logging_lib</description>
    <version>1.0</version>
    <name>Logging_lib</name>
    <organization>
        <name>knoldus</name>
    </organization>
    <dependencies>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>2.13.10</version>
        </dependency>
        <dependency>
            <groupId>com.rollbar</groupId>
            <artifactId>rollbar-java</artifactId>
            <version>1.9.0</version>
        </dependency>
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>6.3</version>
        </dependency>
        <dependency>
            <groupId>com.typesafe.play</groupId>
            <artifactId>play-json_2.13</artifactId>
            <version>2.9.4</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>googleartifactregistry</id>
            <name>google-artifact-registry</name>
            <url>https://asia-maven.pkg.dev/sonarqube-289802/knoldus-aws-lib/</url>
            <layout>default</layout>
        </repository>
    </repositories>
</project>
```