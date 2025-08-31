@echo off
echo Minecraft MQTT Streamer - Building with Docker
echo =============================================

echo.
echo Building plugin using Docker Maven...
docker run --rm -v "%cd%":/usr/src/app -w /usr/src/app maven:3.9.11-eclipse-temurin-21 mvn clean package

if %errorlevel% neq 0 (
    echo.
    echo Error: Failed to build plugin
    echo.
    echo Alternative: Install Java 21 and Maven locally
    echo 1. Download Java 21 from: https://adoptium.net/
    echo 2. Download Maven from: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

echo.
echo =============================================
echo Plugin built successfully!
echo.
echo The JAR file is now in: target/minecraft-mqtt-streamer-1.0.0.jar
echo.
echo Copying plugin to plugins folder...
if not exist plugins mkdir plugins
copy target\minecraft-mqtt-streamer-1.0.0.jar plugins\ /Y
if %errorlevel% equ 0 (
    echo ✅ Plugin copied successfully to plugins folder!
) else (
    echo ❌ Failed to copy plugin to plugins folder
    pause
    exit /b 1
)

echo.
echo =============================================
echo Build and copy completed!
echo.
echo Restarting Minecraft server to load the new plugin...
docker compose restart paper-server
echo.
echo ✅ Server restarted! The new plugin is now active.
echo.
pause
