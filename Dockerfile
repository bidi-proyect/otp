# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
ARG JAR_FILE=target/otp-1.0.0-RELEASE.jar
ADD target/otp-1.0.0-RELEASE.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
