# Build
FROM maven:3.9.10-amazoncorretto-21-alpine AS build

WORKDIR /app
COPY . .

RUN mvn clean package


FROM amazoncorretto:21.0.7-alpine

ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} /app/service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/service.jar"]
