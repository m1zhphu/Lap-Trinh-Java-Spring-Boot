# =========================
# STAGE 1: BUILD (Java 21)
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# =========================
# STAGE 2: RUNTIME (Java 21)
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Tên jar của bạn là demo-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]