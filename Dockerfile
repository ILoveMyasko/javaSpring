# jdk 17 wont work
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY build/libs/lab1-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]