# ---- Build Stage ----
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -B -DskipTests package

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]