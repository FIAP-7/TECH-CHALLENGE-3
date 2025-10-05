FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]