package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.events.WeatherEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.HashMap;
import java.util.Map;

public class WeatherListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public WeatherListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!config.isWeatherEnabled() || event.isCancelled()) {
            return;
        }

        String weather = event.toWeatherState() ? "RAIN" : "CLEAR";
        String worldName = event.getWorld().getName();

        WeatherEvent payload = new WeatherEvent(weather);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("world", worldName);

        plugin.getMqttManager().publish(config.getWeatherTopic(), placeholders, payload);
    }
}
