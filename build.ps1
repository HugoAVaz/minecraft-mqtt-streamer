buildWrite-Host "Minecraft MQTT Streamer - Building with Docker" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

Write-Host ""
Write-Host "Building plugin using Docker Maven..." -ForegroundColor Yellow

# Build using Docker Maven
docker run --rm -v "${PWD}:/usr/src/app" -w /usr/src/app maven:3.9.11-eclipse-temurin-21 mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Error: Failed to build plugin" -ForegroundColor Red
    Write-Host ""
    Write-Host "Alternative: Install Java 21 and Maven locally" -ForegroundColor Yellow
    Write-Host "1. Download Java 21 from: https://adoptium.net/" -ForegroundColor White
    Write-Host "2. Download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host ""
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host ""
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "Plugin built successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "The JAR file is now in: target/minecraft-mqtt-streamer-1.0.0.jar" -ForegroundColor White
Write-Host ""
Write-Host "Copying plugin to plugins folder..." -ForegroundColor Yellow
Copy-Item target\minecraft-mqtt-streamer-1.0.0.jar plugins\ -Force
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Plugin copied successfully to plugins folder!" -ForegroundColor Green
} else {
    Write-Host "❌ Failed to copy plugin to plugins folder" -ForegroundColor Red
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host ""
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "Build and copy completed!" -ForegroundColor Green
Write-Host ""
Write-Host "Restarting Minecraft server to load the new plugin..." -ForegroundColor Yellow
docker compose restart paper-server
Write-Host ""
Write-Host "✅ Server restarted! The new plugin is now active." -ForegroundColor Green
Write-Host ""
Read-Host "Press Enter to continue"
