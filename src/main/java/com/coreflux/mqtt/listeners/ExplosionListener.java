package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.events.ExplosionEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;
import java.util.Map;

public class ExplosionListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public ExplosionListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    private String getBlockId(Block block) {
        return block.getWorld().getName() + "@" + block.getX() + "_" + block.getY() + "_" + block.getZ();
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!config.isTntEventsEnabled() || event.isCancelled()) {
            return;
        }

        if (event.getEntityType() == EntityType.TNT) {
            ExplosionEvent payload = new ExplosionEvent(
                "TNT",
                event.getYield(),
                event.blockList().size(),
                event.getLocation()
            );

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("blockType", "tnt");
            placeholders.put("blockId", getBlockId(event.getLocation().getBlock()));

            plugin.getMqttManager().publish(config.getTntExplosionTopic(), placeholders, payload);
        }
    }
}
