package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class EnemyDamageEvent {
    public final String damagerName;
    public final String damagerType;
    public final double damageAmount;
    public final Map<String, Double> location;
    public final long timestamp;

    public EnemyDamageEvent(String damagerName, String damagerType, double damageAmount, Location loc) {
        this.damagerName = damagerName;
        this.damagerType = damagerType;
        this.damageAmount = damageAmount;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
