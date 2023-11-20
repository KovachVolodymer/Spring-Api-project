FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

FROM openjdk:17-alpine

WORKDIR /app
COPY --from=builder /app/target/spring-boot-security-jwt-mongodb-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]