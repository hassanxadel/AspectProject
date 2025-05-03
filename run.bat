@echo off
echo === Quran Search Service Runner ===
echo Choose build and run method:
echo 1. Simple Docker (fastest)
echo 2. Full Maven build and Docker
echo 3. Docker rebuild (no cache)
choice /c 123 /n /m "Select option (1-3): "

if errorlevel 3 goto DOCKER_REBUILD
if errorlevel 2 goto FULL_BUILD 
if errorlevel 1 goto SIMPLE_RUN

:SIMPLE_RUN
echo === Building and Running Quran Search Service via Docker ===
echo === Stopping any running containers ===
docker-compose down
echo === Building and starting containers ===
docker-compose up --build
goto END

:FULL_BUILD
echo Setting up environment...
rem We'll use the Java in PATH instead of requiring JAVA_HOME
set MAVEN_OPTS=-Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8

echo Building microservices...

echo Building Quran Service...
cd microservices\quran-service
call mvn clean package -DskipTests -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8
if errorlevel 1 (
  echo Build failed. Exiting...
  exit /b 1
)

echo Building Frontend Service...
cd ..\frontend-service
call mvn clean package -DskipTests -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8
if errorlevel 1 (
  echo Build failed. Exiting...
  exit /b 1
)

echo Building API Gateway...
cd ..\api-gateway
call mvn clean package -DskipTests -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8
if errorlevel 1 (
  echo Build failed. Exiting...
  exit /b 1
)

cd ..\..

echo Starting Docker services...
docker-compose down
docker-compose up --build
goto END

:DOCKER_REBUILD
echo Setting up environment...
echo Building and running with Docker (no cache)...
docker-compose down
docker-compose build --no-cache
docker-compose up
goto END

:END
echo Services started! You can access:
echo API Gateway: http://localhost:9080
echo Frontend: http://localhost:9083
echo Quran Service: http://localhost:9082 