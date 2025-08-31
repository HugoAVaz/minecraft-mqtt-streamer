package com.coreflux.mqtt.events;

public class WeatherEvent {
    public final String weather; // e.g., CLEAR, RAIN, THUNDER
    public final long timestamp;

    public WeatherEvent(String weather) {
        this.weather = weather;
        this.timestamp = System.currentTimeMillis();
    }
}
