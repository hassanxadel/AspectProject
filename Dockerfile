# Build stage
FROM maven:3.8-openjdk-8-slim AS build
WORKDIR /app

# Copy the entire project structure
COPY . .

# Build all services
RUN mvn clean package -DskipTests -f microservices/surah-service/pom.xml
RUN mvn clean package -DskipTests -f microservices/ayah-service/pom.xml
RUN mvn clean package -DskipTests -f microservices/frontend-service/pom.xml
RUN mvn clean package -DskipTests -f microservices/api-gateway/pom.xml

# Runtime stage
FROM openjdk:8-jdk-alpine
WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

# Copy all jar files from the build stage
COPY --from=build /app/microservices/surah-service/target/*.jar /app/surah-service.jar
COPY --from=build /app/microservices/ayah-service/target/*.jar /app/ayah-service.jar
COPY --from=build /app/microservices/frontend-service/target/*.jar /app/frontend-service.jar
COPY --from=build /app/microservices/api-gateway/target/*.jar /app/api-gateway.jar

# Create a start script
RUN echo '#!/bin/sh' > /app/start-services.sh && \
    echo 'java -jar /app/surah-service.jar > /app/surah-service.log 2>&1 &' >> /app/start-services.sh && \
    echo 'java -jar /app/ayah-service.jar > /app/ayah-service.log 2>&1 &' >> /app/start-services.sh && \
    echo 'java -jar /app/frontend-service.jar > /app/frontend-service.log 2>&1 &' >> /app/start-services.sh && \
    echo 'java -jar /app/api-gateway.jar > /app/api-gateway.log 2>&1' >> /app/start-services.sh && \
    chmod +x /app/start-services.sh

# Create a non-root user to run the application
RUN addgroup -S spring && adduser -S spring -G spring
# Create directory for application data
RUN mkdir -p /app/data && chown -R spring:spring /app/data

# Set permissions for files
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose all ports
EXPOSE 8080 8081 8082 8083

# Run the start script
ENTRYPOINT ["/app/start-services.sh"] 