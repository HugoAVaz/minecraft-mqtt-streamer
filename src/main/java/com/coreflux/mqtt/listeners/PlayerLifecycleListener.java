package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.events.PlayerDeathEvent;
import com.coreflux.mqtt.events.PlayerJoinEvent;
import com.coreflux.mqtt.events.PlayerQuitEvent;
import com.coreflux.mqtt.events.PlayerRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerLifecycleListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public PlayerLifecycleListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        if (!config.isPlayerDiedEnabled()) {
            return;
        }

        Player player = event.getEntity();
        PlayerDeathEvent payload = new PlayerDeathEvent(
            event.getDeathMessage(),
            player.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerDiedTopic(), placeholders, payload);
    }

    @EventHandler
    public void onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        if (!config.isPlayerRespawnEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerRespawnEvent payload = new PlayerRespawnEvent(
            event.getRespawnLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerRespawnTopic(), placeholders, payload);
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        if (!config.isPlayerJoinEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerJoinEvent payload = new PlayerJoinEvent(player.getLocation());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerJoinTopic(), placeholders, payload);
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        if (!config.isPlayerQuitEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerQuitEvent payload = new PlayerQuitEvent(player.getLocation());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerQuitTopic(), placeholders, payload);
    }
}
