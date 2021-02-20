# Sample Spring Boot Project

The most basic spring boot microservice

Build Tool: Gradle
Language: Kotlin
Spring Boot Version: 2.4
Tags: REST,OpenApi,Actuator,JIB,ktlint,

## How to run?

```
gradle bootRun
````
Server is running so actuator endpoints are responsive.
```
GET http://localhost:8080/actuator/info 
```

```json
{"git":{"branch":"master","commit":{"id":"1adc0ab","time":"2021-02-20T18:33:37Z"}},"build":{"operatingSystem":"Mac OS X (10.16)","artifact":"spring-boot-app","by":"canyaman","group":"me.yaman.can","basePackage":"me.yaman.can.demo","version":"0.0.1-SNAPSHOT","continuousIntegration":"false","build":"0","machine":"Can-MacBook-Pro","name":"spring-boot-app","time":"2021-02-20T19:12:18.116Z"}}
```

```
GET http://localhost:8080/actuator/health
```
```json
{"status":"UP"}
```

## Hot to develop?
First of all, build project after every file changes.
As a background task run the task on the console
```
gradle build --continuous
```

While continuous build running, run the application (console or IDE)
```
gradle bootRun
```
Then you can utilize live reload feature 

## How to validate?

```
gradle test
gradle ktlint
```

## How to deploy?

Build local docker registery container image
```
gradle jibDockerBuild
```
Then run application inside container
```
docker run -p 80:8080 spring-boot-app
```

```
GET http://localhost/actuator/info   
GET http://localhost/actuator/health  
```


