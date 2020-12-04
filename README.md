[![Build Status](https://travis-ci.com/chrisdimaio/id-server.svg?branch=master)](https://travis-ci.com/chrisdimaio/id-server)
![GitHub](https://img.shields.io/github/license/chrisdimaio/poker-evaluator)
# id-server
My own rendition of Twitter's Snowflake ID server.

*Note: Rename to Pearl? Like a snowflake, no two are alike.*
## Start It
### Docker
```shell script
docker-compose -f ./docker-compose.yml up -d --build
```

### Java
```shell script
mvn package
java \
-jar target/IdServer-1.0-SNAPSHOT-fat.jar \
run io.chrisdima.idserver.IDServerVerticle \
-conf src/main/resources/configuration/idserver.json \
-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory
```

## Use It
Call the api.
```shell script
curl http://localhost:8080/api/id
```
Response is a json object containing a unique id.
```json
{
  "id" : "6740422838775455744"
}
```

## Configure It
To configure id-server, modify the contents of *src/main/resources/configuration/idserver.json*
```json
{
  "http.port": 8080,
  "instances": 5,
  "route": "/api/id"
}
```
#### Fields
##### http.port
The port the server runs on.
##### instances
The number of worker threads the server will use.
##### route
The path to api.

