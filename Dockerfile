FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/taskManagementSystem-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]