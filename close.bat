@echo off
echo Minecraft MQTT Streamer - Stopping Environment
echo =============================================
echo.
echo Stopping Docker services...
docker compose down
echo.
echo Environment stopped successfully!
echo.
pause
