# Start with an official Java runtime as a base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the target directory (after building the app) to /app/app.jar
COPY target/*.jar app.jar

# Expose the port on which the app will run
EXPOSE 9090

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
