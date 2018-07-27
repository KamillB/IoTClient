package com.example.kamil.smartrpi.models;

public class WsMessage {
    private String sessionKey;
    private String type;
    private Payload payload;

    public WsMessage(){
    }

    public WsMessage(String sessionKey, String type, Payload payload) {
        this.sessionKey = sessionKey;
        this.type = type;
        this.payload = payload;

    }

    public Payload getPayload() { return payload; }

    public String getSessionKey() { return sessionKey; }

    public String getType() { return type; }

    public void setPayload(Payload payload) { this.payload = payload; }

    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }

    public void setType(String type) { this.type = type; }


}
