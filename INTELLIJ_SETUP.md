# Setting Up Quran Search Service in IntelliJ IDEA

This document provides step-by-step instructions to set up and run the Quran Search Service microservices project in IntelliJ IDEA.

## Project Import

1. Open IntelliJ IDEA
2. Select "Open" or "Import Project"
3. Navigate to the root folder of the project (`search-service`)
4. Select the root `pom.xml` file and click "Open as Project"
5. Wait for Maven to download dependencies and index the project

## Running Microservices Individually

### Setting Up Run Configurations

For each service, create a separate Run Configuration:

1. Click "Run" > "Edit Configurations"
2. Click the "+" button and select "Spring Boot"
3. Create the following configurations:

#### Surah Service
- Name: `SurahService`
- Main class: `com.baraka.surah.SurahServiceApplication`
- VM options: `-Dserver.port=8081`
- Working directory: `$PROJECT_DIR$`
- Module: `surah-service`
- Environment variables: 
  ```
  SPRING_PROFILES_ACTIVE=dev
  ```

#### Ayah Service
- Name: `AyahService`
- Main class: `com.baraka.ayah.AyahServiceApplication`
- VM options: `-Dserver.port=8082`
- Working directory: `$PROJECT_DIR$`
- Module: `ayah-service`
- Environment variables: 
  ```
  SPRING_PROFILES_ACTIVE=dev
  ```

#### Frontend Service
- Name: `FrontendService`
- Main class: `com.baraka.frontend.FrontendServiceApplication`
- VM options: `-Dserver.port=8083`
- Working directory: `$PROJECT_DIR$`
- Module: `frontend-service`
- Environment variables: 
  ```
  SPRING_PROFILES_ACTIVE=dev
  ```

#### API Gateway
- Name: `ApiGateway`
- Main class: `com.baraka.gateway.ApiGatewayApplication`
- VM options: `-Dserver.port=8080`
- Working directory: `$PROJECT_DIR$`
- Module: `api-gateway`
- Environment variables: 
  ```
  SPRING_PROFILES_ACTIVE=dev
  ```

### Running the Services

1. Start services in this order:
   1. `SurahService`
   2. `AyahService`
   3. `FrontendService`
   4. `ApiGateway`

2. Access the application at:
   - API Gateway: http://localhost:8080
   - Surah Service direct: http://localhost:8081
   - Ayah Service direct: http://localhost:8082
   - Frontend Service direct: http://localhost:8083

## Running with Docker

1. Make sure Docker Desktop is running
2. Open a terminal in IntelliJ (View > Tool Windows > Terminal)
3. Run: `docker-compose up --build`
4. Access the application at:
   - API Gateway: http://localhost:9080
   - Surah Service direct: http://localhost:9081
   - Ayah Service direct: http://localhost:9082
   - Frontend Service direct: http://localhost:9083

## Compound Run Configuration

You can also create a compound configuration to start all services at once:

1. Click "Run" > "Edit Configurations"
2. Click the "+" button and select "Compound"
3. Name: "All Services"
4. Add all four services in the correct order
5. Click "Apply" and "OK"

Now you can start all services with a single click.

## Troubleshooting

### Port Already in Use
If you see an error like "Port already in use," you can:
1. Kill the process using that port, or
2. Change the port in the run configuration

### Connection Refused
If services can't connect to each other:
1. Make sure all services are running
2. Check application.properties files for correct service URLs
3. Verify network settings in Docker (if using Docker)

### Docker Issues
If you have issues with Docker:
1. Run `docker-compose down --remove-orphans` to clean up
2. Restart Docker Desktop
3. Run `docker-compose up --build` again 