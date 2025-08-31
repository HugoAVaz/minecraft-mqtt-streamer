package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class PlayerDamageEvent {
    public final String cause;
    public final double damageAmount;
    public final Map<String, Double> location;
    public final long timestamp;

    public PlayerDamageEvent(String cause, double damageAmount, Location loc) {
        this.cause = cause;
        this.damageAmount = damageAmount;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
