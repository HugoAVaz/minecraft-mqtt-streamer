package com.coreflux.mqtt.events;

import java.util.Map;

public class TimeEvent {
    public final long worldTime;
    public final boolean isNight;
    public final long timestamp;

    public TimeEvent(long worldTime, boolean isNight) {
        this.worldTime = worldTime;
        this.isNight = isNight;
        this.timestamp = System.currentTimeMillis();
    }
}
