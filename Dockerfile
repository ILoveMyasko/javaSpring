FROM gradle:8.4-jdk21 AS builder
WORKDIR /workspace

COPY build.gradle settings.gradle gradlew gradle/ ./

RUN gradle --no-daemon dependencies

COPY . .

RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jdk-jammy AS runtime
WORKDIR /app

COPY --from=builder  /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]