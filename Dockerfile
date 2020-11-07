FROM openjdk:9
RUN apt-get update -y && apt-get install maven -y
COPY . idserver/
WORKDIR idserver/
RUN mvn package
EXPOSE 8080
CMD ["java", "-jar", "target/IdServer-1.0-SNAPSHOT-fat.jar", "run", "io.chrisdima.idserver.IdServerVerticle", "-conf", "src/main/resources/configuration/idserver.json"]