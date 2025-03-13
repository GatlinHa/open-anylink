FROM openjdk:8-jre
WORKDIR /app
COPY ./target/anylink-*.jar ./app.jar
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-jar", "app.jar"]

