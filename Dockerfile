ENV PGHOST=${PGHOST}
ENV PGPORT=${PGPORT}
ENV PGDATABASE=${PGDATABASE}
ENV PGUSER=${PGUSER}
ENV PGPASSWORD=${PGPASSWORD}

# Stage 1: Build with Maven using Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies (for better build caching)
COPY Backend/pom.xml .

RUN mvn dependency:go-offline

# Now copy the source code
COPY Backend/src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Allow dynamic port assignment (e.g., for Railway)
ENV PORT=8080
EXPOSE 8080

# Command to run the app
CMD ["java", "-jar", "app.jar"]
