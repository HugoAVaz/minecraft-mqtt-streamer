#!/bin/bash
set -e

echo "Minecraft MQTT Streamer - Building with Docker"
echo "============================================="

echo ""
echo "Building plugin using Docker Maven..."
docker run --rm -v "$(pwd)":/usr/src/app -w /usr/src/app maven:3.9.11-eclipse-temurin-21 mvn clean package

if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Failed to build plugin"
    echo ""
    echo "Alternative: Install Java 21 and Maven locally"
    exit 1
fi

echo ""
echo "Copying plugin to plugins folder..."
mkdir -p plugins
cp target/minecraft-mqtt-streamer-1.0.0.jar plugins/

echo ""
echo "============================================="
echo "Plugin built and copied successfully!"
echo ""
echo "Restarting Minecraft server to load the new plugin..."
docker compose restart paper-server
echo ""
echo "✅ Server restarted! The new plugin is now active."
echo ""
