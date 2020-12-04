[![Build Status](https://travis-ci.com/chrisdimaio/id-server.svg?branch=master)](https://travis-ci.com/chrisdimaio/id-server)
![GitHub](https://img.shields.io/github/license/chrisdimaio/poker-evaluator)
# id-server
My own rendition of Twitter's Snowflake ID server.
## Start It
```shell script
docker-compose -f ./docker-compose.yml up -d --build
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
### Configurations
    http.port: The port the server runs on.
    instances: The number of worker threads the server will use.
    route: The path to api.

