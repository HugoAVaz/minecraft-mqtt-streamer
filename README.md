# Minecraft MQTT Streamer

![Minecraft Coreflux Header](main.png)

The Minecraft MQTT Streamer is a Bukkit plugin that streams in-game events from a Minecraft server to an MQTT broker. It is designed to follow the Unified Namespace (UNS) architecture, providing a structured and scalable way to monitor server and player activities in real-time. This plugin captures a wide range of events, including player actions, enemy engagements, and world changes, and publishes them to specific MQTT topics.

In addition to the plugin, this project also includes a collection of Coreflux LOT Notebook files (`main.lotnb` and `event_counters.lotnb`) that define a status model for processing the raw event data. When loaded into a LOT-compatible MQTT broker, these notebooks create a real-time view of the server's state by aggregating events into a series of status counters.

## About Coreflux

This project is built on the [Coreflux](https://coreflux.org) IoT platform, which provides the MQTT broker, data processing capabilities, and development tools used in this solution. Coreflux is designed for real-time data management and automation. For more details, check out the official [Coreflux Documentation](https://docs.coreflux.org).

## System Components

This solution is composed of four main components working together:

1.  **Minecraft MQTT Streamer Plugin**: A Bukkit plugin that runs on the Minecraft server. It captures a wide range of in-game events and publishes them as structured MQTT messages following the Unified Namespace (UNS) architecture.
2.  **Coreflux MQTT Broker**: A high-performance MQTT broker that acts as the central messaging hub. It receives events from the Minecraft plugin and routes them to subscribers like the web dashboard. It can also execute LOT (Language of Things) code for real-time data processing. The broker is included in the `docker-compose.yml` file for easy setup.
3.  **Web Dashboard**: A web-based interface for real-time monitoring and interaction with the Minecraft server via MQTT. It provides a comprehensive view of the server's status, players, devices, and more.
4.  **LOT Notebooks**: Interactive documents containing LOT code. They process the raw event data from the plugin to generate aggregated status information (e.g., counters for player actions) and enable further automation.

## Prerequisites

Before you begin, ensure you have the following tools installed:

- **Docker and Docker Compose**: Used to build the plugin and run the development environment, including the Minecraft server and Coreflux MQTT broker.
- **Visual Studio Code**: The recommended code editor for working with this project.
- **VS Code Extensions**:
  - **LOT Notebooks by Coreflux**: For creating and managing `.lotnb` notebook files. 
  - **LOT Language Support by Coreflux**: For syntax highlighting and language support for `.lotnb` script files.

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

## LOT Notebooks

The LOT Notebooks are a key component of this project, as they transform the raw event stream into a structured and aggregated status model. They provide an interactive, Jupyter-like development experience for the Language of Things (LOT), allowing you to create complete "System as Code" documents that combine both documentation and executable code in a single file.

By processing raw events like a player crafting an item (`.../events/craft`), the notebooks can generate new, aggregated data streams, such as a counter for crafted items (`.../status/craft/counter`). This enriched data is then used by the web dashboard to display player statistics. This demonstrates how LOT Notebooks can expand the system's capabilities, enabling powerful automation and generating new insights from the raw event data.

### What are LOT Notebooks?
LOT Notebooks (`.lotnb` files) are interactive documents that contain:

- **Markdown cells**: For documentation, explanations, diagrams, and context.
- **LOT code cells**: For executable LOT definitions (Models, Rules, Actions, Routes).

This "System as Code" approach combines clear explanations of your system's architecture and logic with the actual code that gets deployed to your MQTT broker.

### Setting up the LOT Notebook Environment

To get started with LOT Notebooks, you first need to set up your VS Code environment:

1.  **Install the Extensions**:
    -   Open Visual Studio Code.
    -   Go to the Extensions view (Ctrl+Shift+X or Cmd+Shift+X).
    -   Search for and install both of the following extensions:
        -   `LOT Notebooks by Coreflux`
        -   `LOT Language Support by Coreflux`
2.  **Configure Your MQTT Broker Connection**:
    -   Open the Command Palette (Ctrl+Shift+P or Cmd+Shift+P).
    -   Type "LOT Notebook: Change Credentials" and select it.
    -   Enter your broker details. If you are using the `docker-compose.yml` file included in this project, the details are:
        -   **URL**: `mqtt://localhost:1883`
        -   **Username**: `root`
        -   **Password**: `coreflux`

### Deploying the Status Model with LOT Notebooks

After setting up your environment, you can deploy the status model to the MQTT broker directly from the notebook files:

1.  **Open `main.lotnb` in VS Code**.
2.  **Execute the code cells**: Run each code cell in the notebook. The extension will automatically upload the LOT definitions (like `INCLUDE "event_counters.lotnb"` and the `DEFINE ACTION` blocks) to the connected broker.
3.  **Subscribe to Status Topics**: Once the notebooks are loaded, the broker will begin processing events from the Minecraft plugin and publishing the aggregated data to the status topics (e.g., `server/players/magui10/status/craft/counter`). You can then subscribe to these topics using an MQTT client to receive real-time updates.
