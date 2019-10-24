package com.example.severin.kersti;

public class Messages {
    private String inhalt;
    private long timestamp;
    private String typ;

    public Messages(String inhalt, long timestamp, String typ, String from) {
        this.inhalt = inhalt;
        this.timestamp = timestamp;
        this.typ = typ;
        this.from = from;
    }

    public String getFrom() {
        return from;

    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;

    public Messages() {
    }

    public String getInhalt() {
        return inhalt;

    }

    public Messages(String inhalt, long timestamp, String typ) {
        this.inhalt = inhalt;
        this.timestamp = timestamp;
        this.typ = typ;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
