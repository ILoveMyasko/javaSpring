# Используем легковесный образ на базе Alpine с JDK 17
FROM eclipse-temurin:23-jdk-alpine
# Устанавливаем необходимые пакеты
RUN apk add --no-cache tzdata

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл
COPY build/libs/lab1-*.jar app.jar

# Открываем порт приложения
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]