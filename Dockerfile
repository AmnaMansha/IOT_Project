
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Maven files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]