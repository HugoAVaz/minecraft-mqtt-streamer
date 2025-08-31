package com.coreflux.mqtt;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        plugin.getLogger().info("Configuration reloaded.");
    }

    // MQTT Broker Configuration
    public String getBrokerUri() { return plugin.getConfig().getString("mqtt.broker.uri", "tcp://localhost:1883"); }
    public String getBrokerUsername() { return plugin.getConfig().getString("mqtt.broker.username", ""); }
    public String getBrokerPassword() { return plugin.getConfig().getString("mqtt.broker.password", ""); }
    public String getClientId() { return plugin.getConfig().getString("mqtt.broker.client-id", "minecraft-streamer"); }
    public int getQos() { return plugin.getConfig().getInt("mqtt.qos", 1); }
    public boolean isRetain() { return plugin.getConfig().getBoolean("mqtt.retain", false); }

    public String getBaseTopic() { return plugin.getConfig().getString("mqtt.topics.base", "server"); }

    private String resolveTopicTemplate(String topicTemplate) {
        if (topicTemplate == null) return null;
        String resolvedTopic = topicTemplate;
        // Limited iterations to prevent infinite recursion
        for (int i=0; i < 5; i++) {
            if (!resolvedTopic.contains("{")) break;
            resolvedTopic = resolvedTopic.replace("{players.base}", plugin.getConfig().getString("mqtt.topics.players.base", "{base}/players/{playerName}/events"));
            resolvedTopic = resolvedTopic.replace("{devices.base}", plugin.getConfig().getString("mqtt.topics.devices.base", "{base}/devices/{deviceType}/{deviceId}/events"));
            resolvedTopic = resolvedTopic.replace("{special_blocks.base}", plugin.getConfig().getString("mqtt.topics.special_blocks.base", "{base}/specialblocks/{blockType}/{blockId}/events"));
            resolvedTopic = resolvedTopic.replace("{enemies.base}", plugin.getConfig().getString("mqtt.topics.enemies.base", "{base}/enemies/{enemyType}/{enemyId}/events"));
            // base should be last, as other bases might contain it.
            resolvedTopic = resolvedTopic.replace("{base}", plugin.getConfig().getString("mqtt.topics.base", "server"));
        }
        return resolvedTopic;
    }

    // Topic Templates
    public String getTimeTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.world.time", "server/world/time")); }
    public String getWeatherTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.world.weather", "server/world/weather")); }
    public String getPlayerCraftTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.craft", "server/players/{playerName}/events/craft")); }
    public String getPlayerBuildTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.build", "server/players/{playerName}/events/build")); }
    public String getPlayerMineTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.mine", "server/players/{playerName}/events/mine")); }
    public String getPlayerDiedTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.died", "server/players/{playerName}/events/died")); }
    public String getPlayerRespawnTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.respawn", "server/players/{playerName}/events/respawn")); }
    public String getPlayerEatTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.eat", "{players.base}/eat")); }
    public String getPlayerDamageTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.damage", "{players.base}/damage")); }
    public String getPlayerJoinTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.join", "{players.base}/join")); }
    public String getPlayerQuitTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.players.quit", "{players.base}/quit")); }
    public String getEnemyDamageTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.enemies.damage", "{enemies.base}/damage")); }
    public String getEnemyDeathTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.enemies.death", "{enemies.base}/death")); }
    public String getEnemySpawnTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.enemies.spawn", "{enemies.base}/spawn")); }
    public String getCommandExecuteTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.commands.execute", "{base}/commands/execute")); }
    public String getChatMessagesTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.chat.messages", "{base}/chat/messages")); }
    public String getChatSendTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.chat.send", "{base}/chat/send")); }
    public String getFurnaceBurnTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.devices.furnace_burn", "server/devices/furnace/{deviceId}/events/burn")); }
    public String getFurnaceSmeltTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.devices.furnace_smelt", "server/devices/furnace/{deviceId}/events/smelt")); }
    public String getFurnaceExtractTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.devices.furnace_extract", "server/devices/furnace/{deviceId}/events/extract")); }
    public String getTntExplosionTopic() { return resolveTopicTemplate(plugin.getConfig().getString("mqtt.topics.special_blocks.tnt_explosion", "server/specialblocks/tnt/{blockId}/events/explosion")); }

    // Event Enables
    public boolean isTimeEnabled() { return plugin.getConfig().getBoolean("events.time.enabled", true); }
    public int getTimeInterval() { return plugin.getConfig().getInt("events.time.interval", 15); }
    
    public boolean isWeatherEnabled() { return plugin.getConfig().getBoolean("events.weather.enabled", true); }

    public boolean isPlayerEventsEnabled() { return plugin.getConfig().getBoolean("events.players.enabled", true); }
    public boolean isPlayerCraftEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.craft", true); }
    public boolean isPlayerBuildEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.build", true); }
    public boolean isPlayerMineEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.mine", true); }
    public boolean isPlayerDiedEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.died", true); }
    public boolean isPlayerRespawnEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.respawn", true); }
    public boolean isPlayerEatEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.eat", true); }
    public boolean isPlayerDamageEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.damage", true); }
    public boolean isPlayerJoinEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.join", true); }
    public boolean isPlayerQuitEnabled() { return isPlayerEventsEnabled() && plugin.getConfig().getBoolean("events.players.quit", true); }
    
    public boolean isDeviceEventsEnabled() { return plugin.getConfig().getBoolean("events.devices.enabled", true); }
    public boolean isFurnaceEventsEnabled() { return isDeviceEventsEnabled() && plugin.getConfig().getBoolean("events.devices.furnace", true); }

    public boolean isSpecialBlockEventsEnabled() { return plugin.getConfig().getBoolean("events.special_blocks.enabled", true); }
    public boolean isTntEventsEnabled() { return isSpecialBlockEventsEnabled() && plugin.getConfig().getBoolean("events.special_blocks.tnt", true); }

    public boolean isEnemyEventsEnabled() { return plugin.getConfig().getBoolean("events.enemies.enabled", true); }
    public boolean isEnemyDamageEnabled() { return isEnemyEventsEnabled() && plugin.getConfig().getBoolean("events.enemies.damage", true); }
    public boolean isEnemyDeathEnabled() { return isEnemyEventsEnabled() && plugin.getConfig().getBoolean("events.enemies.death", true); }
    public boolean isEnemySpawnEnabled() { return isEnemyEventsEnabled() && plugin.getConfig().getBoolean("events.enemies.spawn", true); }

    public boolean isCommandsEnabled() { return plugin.getConfig().getBoolean("commands.enabled", true); }

    public boolean isChatEnabled() { return plugin.getConfig().getBoolean("chat.enabled", true); }

    // Logging
    public String getLogLevel() { return plugin.getConfig().getString("logging.level", "INFO"); }
    public boolean isMqttDebug() { return plugin.getConfig().getBoolean("logging.mqtt-debug", false); }
    public boolean isEventDebug() { return plugin.getConfig().getBoolean("logging.event-debug", false); }
}
