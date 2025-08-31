# Minecraft MQTT Streamer

![Minecraft Coreflux Header](main.png)

The Minecraft MQTT Streamer is a Bukkit plugin that streams in-game events from a Minecraft server to an MQTT broker. It is designed to follow the Unified Namespace (UNS) architecture, providing a structured and scalable way to monitor server and player activities in real-time. This plugin captures a wide range of events, including player actions, enemy engagements, and world changes, and publishes them to specific MQTT topics.

In addition to the plugin, this project also includes a collection of Coreflux LOT Notebook files that define a status model for processing the raw event data. When loaded into a LOT-compatible MQTT broker, these notebooks create a real-time view of the server's state by aggregating events into a series of status counters.

## Getting Started

This guide will walk you through setting up the development environment, which includes the Minecraft server, MQTT broker, and the web dashboard.

### Prerequisites

-   **Docker and Docker Compose**: Essential for running the entire environment.
-   **Minecraft Java Edition Client**: Version 1.21 or later to connect to the server.
-   **(Optional) Java 21 and Maven**: If you prefer to build the plugin locally without Docker.

### Build and Run

Convenient scripts are provided to build the plugin and start the environment.

#### For Windows Users:

1.  **Build the plugin**:
    ```shell
    .\build.bat
    ```
    This script uses Docker to compile the Java plugin. The resulting `.jar` file is automatically copied to the `plugins` directory.

2.  **Start the services**:
    ```shell
    .\start.bat
    ```
    This command starts the Minecraft server, Coreflux MQTT broker, and the web dashboard using `docker-compose`.

3.  **Stop the services**:
    ```shell
    .\close.bat
    ```
    This command gracefully stops all the running services.

#### For Linux and macOS Users:

1.  **Build the plugin**:
    ```shell
    chmod +x build.sh
    ./build.sh
    ```
    This script uses Docker to compile the Java plugin. The resulting `.jar` file is automatically copied to the `plugins` directory.

2.  **Start the services**:
    ```shell
    chmod +x start.sh
    ./start.sh
    ```
    This command starts the Minecraft server, Coreflux MQTT broker, and the web dashboard using `docker-compose`.

3.  **Stop the services**:
    ```shell
    chmod +x close.sh
    ./close.sh
    ```
    This command gracefully stops all the running services.

### Accessing the Services

Once the services are running, you can connect to them:

-   **Minecraft Server**: Open your Minecraft client and connect to `localhost:25565`.
-   **Web Dashboard**: The `start` script will attempt to open the web dashboard automatically in your default browser at `http://localhost:8080`. If it doesn't open, you can navigate to the URL manually.
-   **MQTT Broker**: You can connect to the broker at `localhost:1883` with a client like [MQTT Explorer](http://mqtt-explorer.com/) or any other MQTT client. Use the following credentials:
    -   **Username**: `root`
    -   **Password**: `coreflux`
    
    Subscribe to the topic `server/#` to see all the real-time events.

## How it Works

This GIF demonstrates the real-time interaction between Minecraft and an MQTT client. As events happen in the game, they are instantly published to the MQTT broker, where they can be monitored.

![How it Works](minecraft-mqtt-stream.gif)

## System Components

This solution is composed of four main components working together:

1.  **Minecraft MQTT Streamer Plugin**: A Bukkit plugin that runs on the Minecraft server. It captures a wide range of in-game events and publishes them as structured MQTT messages following the Unified Namespace (UNS) architecture.
2.  **Coreflux MQTT Broker**: A high-performance MQTT broker that acts as the central messaging hub. It receives events from the Minecraft plugin and routes them to subscribers like the web dashboard. It can also execute LOT (Language of Things) code for real-time data processing. The broker is included in the `docker-compose.yml` file for easy setup.
3.  **Web Dashboard**: A web-based interface for real-time monitoring and interaction with the Minecraft server via MQTT. It provides a comprehensive view of the server's status, players, devices, and more.
4.  **LOT Notebooks**: Interactive documents containing LOT code. They process the raw event data from the plugin to generate aggregated status information (e.g., counters for player actions) and enable further automation.

## Features

- **Unified Namespace (UNS)**: The plugin and the accompanying LOT Notebooks are designed around the UNS architecture, ensuring that all MQTT topics are structured in a consistent and predictable way.
- **Real-Time Event Streaming**: Events are published to the MQTT broker as they happen, providing a live feed of server activities.
- **Comprehensive Event Coverage**: The plugin captures a wide range of events, including:
  - **Player Events**: Crafting, building, mining, dying, respawning, eating, and taking damage.
  - **Enemy Events**: Taking damage and dying.
  - **World Events**: Time changes and weather changes.
- **Configurable**: All events can be individually enabled or disabled in the `config.yml` file.
- **LOT Notebook Status Model**: The included `.lotnb` files define a complete status model that can be deployed on a LOT-compatible MQTT broker to automatically generate and update status counters for all tracked events.
- **Web Dashboard**: A comprehensive web interface to monitor and interact with the server in real-time.

## Web Dashboard

The included web dashboard provides a real-time window into your Minecraft server, with the following views:

-   **Overview**: An at-a-glance view of the server's status, including the number of online players, the current world time (day/night), and the weather.
-   **Players**: A detailed view of each player, showing their online status, current location, and aggregated stats (e.g., number of blocks mined, items crafted). It also allows for direct interaction through commands to kill, teleport, or give items to players.
-   **Devices**: Monitor the status of in-game devices. For example, you can see if a furnace is idle, burning fuel, or smelting items.
-   **Enemies**: Track hostile mobs, showing their type, location, and status (alive or dead).
-   **Event Log**: A live, raw feed of all MQTT messages flowing through the broker, invaluable for debugging and understanding the data streams.
-   **Console**: A remote console that allows you to execute any command on the Minecraft server directly from the web interface.
-   **Chat**: A real-time view of the in-game chat, with the ability to send messages to all players from the dashboard.

## Expanding Automation with LOT Notebooks

To go further and customize the data processing and automation, you can use LOT Notebooks.

### What are LOT Notebooks?
LOT Notebooks are interactive documents (`.lotnb` files) that allow you to write and execute LOT (Language of Things) code directly against the MQTT broker. They are a key component for transforming the raw event stream into a structured and aggregated status model.

By processing raw events (e.g., `.../events/craft`), the notebooks can generate new, aggregated data streams (e.g., `.../status/craft/counter`). This enriched data is what the web dashboard uses to display player statistics. This demonstrates how LOT Notebooks can expand the system's capabilities, enabling powerful automation and generating new insights from the raw event data.

### Setting up the Environment
To work with LOT Notebooks, you need to set up your VS Code environment:

1.  **Install Visual Studio Code**.
2.  **Install the Extensions**:
        -   `LOT Notebooks by Coreflux`
        -   `LOT Language Support by Coreflux`
3.  **Configure Your MQTT Broker Connection**:
    -   Open the Command Palette (Ctrl+Shift+P) and run `LOT Notebook: Change Credentials`.
    -   Enter your broker details (URL: `mqtt://localhost:1883`, User: `root`, Pass: `coreflux`).

After setup, you can open `main.lotnb` and execute the code cells to deploy the status model to the broker.

## About Coreflux
This project is built on the [Coreflux](https://coreflux.org) platform, which provides the MQTT broker, data processing capabilities, and development tools used in this solution. For more details, check out the official [Coreflux Documentation](https://docs.coreflux.org).
