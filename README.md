# Quran Search Service

This is a microservices-based search application for Quranic content.

## Project Structure

- **quran-service**: The main service that handles Quran data (surahs and ayahs)
- **api-gateway**: API Gateway to route requests to appropriate services
- **frontend-service**: Simple web UI to search and browse Quranic content

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- Docker and Docker Compose

## Running the Application

### Option 1: Using Docker Compose Only (Recommended)

1. Make sure Docker and Docker Compose are running
2. Run the application using:

```
build-and-run-simple.bat
```

This will build all services using Docker and run them.

### Option 2: Build Locally and Run

1. Make sure Maven and Java 8+ are installed
2. Run the build-and-run script:

```
build-and-run.bat
```

This will build each microservice locally with Maven and then start the Docker containers.

## Accessing the Application

- **Frontend UI**: http://localhost:9083
- **API Gateway**: http://localhost:9080
- **Direct Quran Service**: http://localhost:9082

## API Documentation

API documentation is available via Swagger UI:

- Quran Service API: http://localhost:9082/swagger-ui.html

## Troubleshooting

### Java Version Issues

If you encounter Java version errors during build, make sure your JAVA_HOME is set to a Java 8 JDK:

```
set JAVA_HOME=C:\path\to\your\java8\jdk
```

### Docker Issues

If Docker fails to build containers, try:

1. Stop all containers: `docker-compose down`
2. Remove all related images: `docker rmi $(docker images | grep quran | awk '{print $3}')`
3. Rebuild: `docker-compose up --build`

## Database Access

The application uses an in-memory H2 database. You can access the database console at:
http://localhost:9082/h2-console

Connection details:
- JDBC URL: `jdbc:h2:mem:qurandb`
- Username: `sa`
- Password: `password` 