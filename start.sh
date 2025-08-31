#!/bin/bash
set -e

echo "Minecraft MQTT Streamer - Starting Environment"
echo "============================================="

echo ""
echo "Starting Docker services..."
docker compose up -d

echo ""
echo "Waiting for services to start..."
sleep 10

echo ""
echo "Checking service status..."
docker compose ps

echo ""
echo "============================================="
echo "Environment started successfully!"
echo ""
echo "Minecraft Server: localhost:25565"
echo "MQTT Broker: localhost:1883"
echo ""
echo "To view logs: docker compose logs -f"
echo "To stop: docker compose down"
echo ""
