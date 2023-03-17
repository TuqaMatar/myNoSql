# Use the official Maven OpenJDK image as the base image
FROM maven:3.8.3-openjdk-8 as build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file into the container
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Start a new stage with OpenJDK JRE as the base image
FROM openjdk:8-jre-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
