package com.coreflux.mqtt.mqtt;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Map;
import java.util.UUID;

public class MQTTManager {
    
    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;
    private final ObjectMapper objectMapper;
    private MqttClient mqttClient;
    private boolean isConnected = false;
    
    public MQTTManager(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        this.objectMapper = new ObjectMapper();
        connect();
    }
    
    public void connect() {
        try {
            String clientId = config.getClientId() + "-" + UUID.randomUUID().toString().substring(0, 8);
            mqttClient = new MqttClient(config.getBrokerUri(), clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(config.getBrokerUsername());
            connOpts.setPassword(config.getBrokerPassword().toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);

            plugin.getLogger().info("Connecting to MQTT broker: " + config.getBrokerUri());
            mqttClient.connect(connOpts);
            isConnected = true;
            plugin.getLogger().info("Connected to MQTT broker: " + config.getBrokerUri());
        } catch (MqttException e) {
            plugin.getLogger().severe("Failed to connect to MQTT broker: " + e.getMessage());
            if (config.isMqttDebug()) {
                e.printStackTrace();
            }
        }
    }
    
    public void disconnect() {
        if (isConnected && mqttClient != null) {
            try {
                mqttClient.disconnect();
                isConnected = false;
                plugin.getLogger().info("Disconnected from MQTT broker.");
            } catch (MqttException e) {
                plugin.getLogger().severe("Error disconnecting from MQTT broker: " + e.getMessage());
            }
        }
    }

    public void publish(String topicTemplate, Map<String, String> placeholders, Object payload) {
        if (!isConnected) {
            return;
        }

        String topic = formatTopic(topicTemplate, placeholders);

        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            MqttMessage message = new MqttMessage(jsonPayload.getBytes());
            message.setQos(config.getQos());
            message.setRetained(config.isRetain());
            mqttClient.publish(topic, message);

            if (config.isMqttDebug()) {
                plugin.getLogger().info("Published to " + topic + ": " + jsonPayload);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to publish message to " + topic + ": " + e.getMessage());
        }
    }

    private String formatTopic(String template, Map<String, String> placeholders) {
        String topic = template;
        
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                topic = topic.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return topic;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
}
