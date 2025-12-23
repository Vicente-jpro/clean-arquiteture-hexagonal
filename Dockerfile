# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom first
COPY pom.xml .

# Copy module poms
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/
COPY bootstrap/pom.xml bootstrap/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src
COPY bootstrap/src bootstrap/src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/bootstrap/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
