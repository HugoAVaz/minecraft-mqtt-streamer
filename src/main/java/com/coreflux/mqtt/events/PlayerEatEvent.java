package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class PlayerEatEvent {
    public final String item;
    public final Map<String, Double> location;
    public final long timestamp;

    public PlayerEatEvent(String item, Location loc) {
        this.item = item;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
