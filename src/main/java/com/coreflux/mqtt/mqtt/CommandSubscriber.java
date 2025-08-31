package com.coreflux.mqtt.mqtt;

import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.events.ChatMessageEvent;
import org.bukkit.Bukkit;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class CommandSubscriber {
    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;
    private MqttClient mqttClient;

    public CommandSubscriber(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        connect();
    }

    private void connect() {
        try {
            String clientId = config.getClientId() + "-subscriber-" + UUID.randomUUID().toString().substring(0, 8);
            mqttClient = new MqttClient(config.getBrokerUri(), clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(config.getBrokerUsername());
            connOpts.setPassword(config.getBrokerPassword().toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);

            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    plugin.getLogger().info("Subscriber connected to MQTT broker. Subscribing to command topic...");
                    try {
                        mqttClient.subscribe(config.getCommandExecuteTopic(), config.getQos());
                        if (config.isChatEnabled()) {
                            mqttClient.subscribe(config.getChatSendTopic(), config.getQos());
                        }
                    } catch (MqttException e) {
                        plugin.getLogger().severe("Failed to subscribe to command topic: " + e.getMessage());
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    plugin.getLogger().warning("Subscriber connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    
                    if (topic.equals(config.getCommandExecuteTopic())) {
                        plugin.getLogger().info("Received command: " + payload);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), payload);
                        });
                    } else if (topic.equals(config.getChatSendTopic())) {
                        plugin.getLogger().info("Received chat message: " + payload);
                        
                        // Broadcast to in-game chat
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.broadcastMessage("§e[Dashboard] §f" + payload);
                        });

                        // Re-publish to the chat messages topic for the dashboard
                        ChatMessageEvent chatPayload = new ChatMessageEvent("Dashboard", payload);
                        plugin.getMqttManager().publish(config.getChatMessagesTopic(), null, chatPayload);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            plugin.getLogger().info("Connecting subscriber to MQTT broker: " + config.getBrokerUri());
            mqttClient.connect(connOpts);

        } catch (MqttException e) {
            plugin.getLogger().severe("Failed to connect subscriber to MQTT broker: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                plugin.getLogger().severe("Error disconnecting subscriber: " + e.getMessage());
            }
        }
    }
}
