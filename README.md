# Minecraft MQTT Streamer

![Minecraft Coreflux Header](main.png)

The Minecraft MQTT Streamer is a Bukkit plugin that streams in-game events from a Minecraft server to an MQTT broker. It is designed to follow the Unified Namespace (UNS) architecture, providing a structured and scalable way to monitor server and player activities in real-time. This plugin captures a wide range of events, including player actions, enemy engagements, and world changes, and publishes them to specific MQTT topics.

In addition to the plugin, this project also includes a collection of LOT Notebook files (`main.lotnb` and `event_counters.lotnb`) that define a status model for processing the raw event data. When loaded into a LOT-compatible MQTT broker, these notebooks create a real-time view of the server's state by aggregating events into a series of status counters.

## Prerequisites

Before you begin, ensure you have the following tools installed:

- **Docker and Docker Compose**: Used to build the plugin and run the development environment, including the Minecraft server and MQTT broker.
- **Visual Studio Code**: The recommended code editor for working with this project.
- **VS Code Extensions**:
  - **LOT Notebooks by Coreflux**: For creating and managing `.lotnb` notebook files.
  - **LOT Language Support by Coreflux**: For syntax highlighting and language support for `.lot` script files.

## Features

- **Unified Namespace (UNS)**: The plugin and the accompanying LOT Notebooks are designed around the UNS architecture, ensuring that all MQTT topics are structured in a consistent and predictable way.
- **Real-Time Event Streaming**: Events are published to the MQTT broker as they happen, providing a live feed of server activities.
- **Comprehensive Event Coverage**: The plugin captures a wide range of events, including:
  - **Player Events**: Crafting, building, mining, dying, respawning, eating, and taking damage.
  - **Enemy Events**: Taking damage and dying.
  - **World Events**: Time changes and weather changes.
- **Configurable**: All events can be individually enabled or disabled in the `config.yml` file.
- **LOT Notebook Status Model**: The included `.lotnb` files define a complete status model that can be deployed on a LOT-compatible MQTT broker to automatically generate and update status counters for all tracked events.

## LOT Notebooks

The LOT Notebooks are a key component of this project, as they transform the raw event stream into a structured and aggregated status model. They provide an interactive, Jupyter-like development experience for the Language of Things (LOT), allowing you to create complete "System as Code" documents that combine both documentation and executable code in a single file.

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
