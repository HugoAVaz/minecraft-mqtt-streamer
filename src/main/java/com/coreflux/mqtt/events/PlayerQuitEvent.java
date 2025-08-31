package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class PlayerQuitEvent {
    public final Map<String, Double> location;
    public final long timestamp;

    public PlayerQuitEvent(Location loc) {
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
