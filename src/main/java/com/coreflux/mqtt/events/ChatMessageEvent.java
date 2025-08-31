package com.coreflux.mqtt.events;

public class ChatMessageEvent {
    public final String playerName;
    public final String message;
    public final long timestamp;

    public ChatMessageEvent(String playerName, String message) {
        this.playerName = playerName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
