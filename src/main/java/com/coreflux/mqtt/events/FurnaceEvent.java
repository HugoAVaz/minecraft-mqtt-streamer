package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class FurnaceEvent {
    public final String action; // e.g., BURN, SMELT, EXTRACT
    public final String material;
    public final String fuel;
    public final int cookTime;
    public final int burnTime;
    public final int amount;
    public final Map<String, Double> location;
    public final long timestamp;

    public FurnaceEvent(String action, String material, String fuel, int cookTime, int burnTime, int amount, Location loc) {
        this.action = action;
        this.material = material;
        this.fuel = fuel;
        this.cookTime = cookTime;
        this.burnTime = burnTime;
        this.amount = amount;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
