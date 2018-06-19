package com.example.kamil.smartrpi.models;

public class MessageWS {
    private String type;
    private TempData tempData;
    
    public MessageWS() {}

    public MessageWS(String type, TempData tempData) {
        this.type = type;
        this.tempData = tempData;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public TempData getTempData() { return tempData; }
    public void setTempData(TempData tempData) {
        this.tempData = tempData;
    }
}
