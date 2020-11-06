FROM openjdk:9
RUN apt-get update -y && apt-get install maven -y
COPY . idserver/
WORKDIR idserver/
RUN mvn package
EXPOSE 8080
CMD ["java", "io.chrisdima.idserver.IDServerLauncher", "run io.chrisdima.idserver.IdServerVerticle", "-conf", "src/main/resources/configuration/idserver.json"]