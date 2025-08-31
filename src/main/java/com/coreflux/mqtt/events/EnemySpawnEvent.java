package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class EnemySpawnEvent {
    public final Map<String, Double> location;
    public final long timestamp;

    public EnemySpawnEvent(Location loc) {
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
