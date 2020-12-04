[![Build Status](https://travis-ci.com/chrisdimaio/id-server.svg?branch=master)](https://travis-ci.com/chrisdimaio/id-server)
![GitHub](https://img.shields.io/github/license/chrisdimaio/poker-evaluator)
# id-server
My own rendition of Twitter's Snowflake ID server.
## How-to run
### Start the container
```shell script
docker-compose -f ./docker-compose.yml up -d --build
```

### Test the server
run
```shell script
curl http://localhost:8080/api/id
```
output
```json
{
  "id" : "6740422838775455744"
}
```


