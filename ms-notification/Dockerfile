FROM eclipse-temurin:17-jre-alpine

LABEL dev.ime.version="1.0"
LABEL dev.ime.maintainer="IvanM"
LABEL dev.ime.description="Just a simple dockerfile"

EXPOSE 8084/TCP

WORKDIR /app

COPY ./target/*.jar ./app.jar

ENTRYPOINT [ "java", "-jar", "./app.jar" ]