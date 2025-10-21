# Stage 1: Build with Maven using Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the pom.xml and download dependencies first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy the source code
COPY src ./src

# Package the app (no tests for speed)
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Support dynamic port setting for Railway
ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
