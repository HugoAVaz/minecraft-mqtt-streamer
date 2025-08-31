package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.events.CraftingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftingListener implements Listener {
    
    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;
    
    public CraftingListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }
    
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!config.isPlayerCraftEnabled()) {
            return;
        }

        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();
        
        List<String> recipeMatrix = Arrays.stream(event.getInventory().getMatrix())
            .map(item -> item != null ? item.getType().toString() : "AIR")
            .collect(Collectors.toList());

        CraftingEvent payload = new CraftingEvent(
            recipeMatrix,
            result.getType().toString(),
            result.getAmount(),
            player.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerCraftTopic(), placeholders, payload);
    }
}
