FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /workspace

COPY task-scheduler-service/ .

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon
RUN JAR_PATH="$(ls build/libs/*.jar | sed -n '/-plain.jar/!p' | head -n 1)" \
    && cp "${JAR_PATH}" app.jar

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=docker

COPY --from=builder /workspace/app.jar /app/task-scheduler-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/task-scheduler-service.jar"]