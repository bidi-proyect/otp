FROM openjdk:17-jdk
VOLUME "/tmp"
EXPOSE 8080
AR JAR_FILE=target/otp-1.0.0-RELEASE
add ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
