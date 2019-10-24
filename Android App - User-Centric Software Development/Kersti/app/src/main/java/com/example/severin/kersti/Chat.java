package com.example.severin.kersti;

public class Chat {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public Chat(long timestamp) {
        this.timestamp = timestamp;
    }

    public Chat() {
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;

    }
}
