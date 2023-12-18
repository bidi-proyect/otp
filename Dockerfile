# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
COPY target/otp-1.0.0-RELEASE.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
