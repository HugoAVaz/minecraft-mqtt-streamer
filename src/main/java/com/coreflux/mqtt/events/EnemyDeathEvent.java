package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class EnemyDeathEvent {
    public final String killerName;
    public final String killerType;
    public final Map<String, Double> location;
    public final long timestamp;

    public EnemyDeathEvent(String killerName, String killerType, Location loc) {
        this.killerName = killerName;
        this.killerType = killerType;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
