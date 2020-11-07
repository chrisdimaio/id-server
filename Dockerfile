FROM openjdk:9
RUN apt-get update -y && apt-get install maven -y
COPY . idserver/
WORKDIR idserver/
RUN mvn package
EXPOSE 8080
CMD ["java", "-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory", "-jar", "target/IdServer-1.0-SNAPSHOT-fat.jar", "run", "io.chrisdima.idserver.IDServerVerticle", "-conf", "src/main/resources/configuration/idserver.json"]