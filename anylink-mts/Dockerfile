FROM openjdk:8-jre
WORKDIR /app
COPY ./target/anylink-*.jar ./app.jar
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-jar", "app.jar", "--dubbo.registry.parameters.namespace=dev20250303001", "--dubbo.registry.address=nacos://172.18.1.1:8848", "--obs.minio.pre-sign=false"]

