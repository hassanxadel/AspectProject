FROM maven:3.8-openjdk-8 as build
WORKDIR /workspace/app

# Copy POM file and download dependencies first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src src/

# Build the application
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"] 