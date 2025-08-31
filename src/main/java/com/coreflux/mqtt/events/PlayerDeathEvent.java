package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class PlayerDeathEvent {
    public final String deathMessage;
    public final Map<String, Double> location;
    public final long timestamp;

    public PlayerDeathEvent(String deathMessage, Location loc) {
        this.deathMessage = deathMessage;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
