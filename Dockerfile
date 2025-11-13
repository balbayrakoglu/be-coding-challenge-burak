# ---- Build Stage ----
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /workspace/app
COPY . .
RUN gradle clean bootJar --no-daemon

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]