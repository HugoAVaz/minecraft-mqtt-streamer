package com.coreflux.mqtt;

import com.coreflux.mqtt.commands.MQTTCommand;
import com.coreflux.mqtt.listeners.*;
import com.coreflux.mqtt.mqtt.CommandSubscriber;
import com.coreflux.mqtt.mqtt.MQTTManager;
import com.coreflux.mqtt.tasks.TimeTask;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftMQTTStreamer extends JavaPlugin {
    
    private static MinecraftMQTTStreamer instance;
    private MQTTManager mqttManager;
    private ConfigManager configManager;
    private TimeTask timeTask;
    private CommandSubscriber commandSubscriber;
    
    @Override
    public void onEnable() {
        instance = this;
        
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        mqttManager = new MQTTManager(this);
        if (configManager.isCommandsEnabled()) {
            commandSubscriber = new CommandSubscriber(this);
        }
        
        registerListeners();
        registerCommands();
        startTasks();

        getLogger().info("Minecraft MQTT Streamer enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        if (timeTask != null) {
            timeTask.cancel();
        }
        if (mqttManager != null) {
            mqttManager.disconnect();
        }
        if (commandSubscriber != null) {
            commandSubscriber.disconnect();
        }
        getLogger().info("Minecraft MQTT Streamer disabled!");
    }
    
    public void reload() {
        configManager.reload();
        
        if (timeTask != null) {
            timeTask.cancel();
        }
        
        if (mqttManager != null) {
            mqttManager.disconnect();
        }
        if (commandSubscriber != null) {
            commandSubscriber.disconnect();
        }

        mqttManager = new MQTTManager(this);
        if (configManager.isCommandsEnabled()) {
            commandSubscriber = new CommandSubscriber(this);
        }
        startTasks();
        getLogger().info("Configuration reloaded!");
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CraftingListener(this), this);
        getServer().getPluginManager().registerEvents(new FurnaceListener(this), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerActionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLifecycleListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConsumeListener(this), this);
        if (configManager.isChatEnabled()) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
    }
    
    private void registerCommands() {
        getCommand("mqtt").setExecutor(new MQTTCommand(this));
    }
    
    private void startTasks() {
        if (configManager.isTimeEnabled()) {
            timeTask = new TimeTask(this);
            timeTask.runTaskTimer(this, 0, configManager.getTimeInterval() * 20L);
        }
    }
    
    public static MinecraftMQTTStreamer getInstance() {
        return instance;
    }
    
    public MQTTManager getMqttManager() {
        return mqttManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
