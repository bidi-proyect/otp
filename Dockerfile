# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
VOLUME "/tmp"
EXPOSE 8080
ARG JAR_FILE=target/otp-1.0.0-RELEASE.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
