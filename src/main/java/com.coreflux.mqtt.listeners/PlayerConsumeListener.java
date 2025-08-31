package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.events.PlayerEatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerConsumeListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public PlayerConsumeListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        if (!config.isPlayerEatEnabled() || event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack consumedItem = event.getItem();

        PlayerEatEvent payload = new PlayerEatEvent(
            consumedItem.getType().toString(),
            player.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerEatTopic(), placeholders, payload);
    }
}
