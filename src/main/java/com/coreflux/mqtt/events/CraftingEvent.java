package com.coreflux.mqtt.events;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class CraftingEvent {
    public final List<String> recipe;
    public final String result;
    public final int amount;
    public final Map<String, Double> location;
    public final long timestamp;

    public CraftingEvent(List<String> recipe, String result, int amount, Location loc) {
        this.recipe = recipe;
        this.result = result;
        this.amount = amount;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
