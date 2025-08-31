package com.coreflux.mqtt.events;

import org.bukkit.Location;
import java.util.Map;

public class ExplosionEvent {
    public final String entity;
    public final float yield;
    public final int blockCount;
    public final Map<String, Double> location;
    public final long timestamp;

    public ExplosionEvent(String entity, float yield, int blockCount, Location loc) {
        this.entity = entity;
        this.yield = yield;
        this.blockCount = blockCount;
        this.location = Map.of("x", loc.getX(), "y", loc.getY(), "z", loc.getZ());
        this.timestamp = System.currentTimeMillis();
    }
}
