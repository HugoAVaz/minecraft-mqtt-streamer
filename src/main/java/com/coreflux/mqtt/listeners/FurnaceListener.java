package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.events.FurnaceEvent;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FurnaceListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public FurnaceListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    private String getDeviceId(Furnace furnace) {
        return furnace.getWorld().getName() + "@" + furnace.getX() + "_" + furnace.getY() + "_" + furnace.getZ();
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (!config.isFurnaceEventsEnabled()) return;

        Furnace furnace = (Furnace) event.getBlock().getState();
        FurnaceEvent payload = new FurnaceEvent(
            "BURN",
            null,
            event.getFuel().getType().toString(),
            0,
            event.getBurnTime(),
            event.getFuel().getAmount(),
            furnace.getLocation()
        );
        
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("deviceType", "furnace");
        placeholders.put("deviceId", getDeviceId(furnace));

        plugin.getMqttManager().publish(config.getFurnaceBurnTopic(), placeholders, payload);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (!config.isFurnaceEventsEnabled()) return;

        Furnace furnace = (Furnace) event.getBlock().getState();
        FurnaceEvent payload = new FurnaceEvent(
            "SMELT",
            event.getSource().getType().toString(),
            null,
            furnace.getCookTimeTotal(),
            0,
            event.getResult().getAmount(),
            furnace.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("deviceType", "furnace");
        placeholders.put("deviceId", getDeviceId(furnace));

        plugin.getMqttManager().publish(config.getFurnaceSmeltTopic(), placeholders, payload);
    }

    @EventHandler
    public void onFurnaceExtract(InventoryClickEvent event) {
        if (!config.isFurnaceEventsEnabled()) return;

        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.FURNACE) {
            return;
        }

        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            FurnaceInventory inventory = (FurnaceInventory) event.getClickedInventory();
            Furnace furnace = inventory.getHolder();
            ItemStack result = event.getCurrentItem();

            if (furnace == null || result == null) return;
            
            FurnaceEvent payload = new FurnaceEvent(
                "EXTRACT",
                result.getType().toString(),
                null,
                0,
                0,
                result.getAmount(),
                furnace.getLocation()
            );

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("deviceType", "furnace");
            placeholders.put("deviceId", getDeviceId(furnace));

            plugin.getMqttManager().publish(config.getFurnaceExtractTopic(), placeholders, payload);
        }
    }
}
