# Multi-stage Dockerfile for the Product Management Application

# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the parent pom and module poms
COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the source code
COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/infrastructure/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
