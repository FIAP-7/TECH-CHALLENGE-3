FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY service-agendamento/pom.xml ./service-agendamento/
COPY service-auth/pom.xml ./service-auth/
COPY service-historico/pom.xml ./service-historico/
COPY service-notificacoes/pom.xml ./service-notificacoes/

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests -pl service-historico -am spring-boot:repackage

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/service-historico/target/service-historico-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]