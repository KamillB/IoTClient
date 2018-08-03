package com.example.kamil.smartrpi.models;

public class Sensor {
    protected String name;
    protected String ownerDevice;

    public Sensor(){
    }

    public Sensor(String name, String ownerDevice) {
        this.name = name;
        this.ownerDevice = ownerDevice;
    }

    public String getName() {
        return name;
    }

    public String getOwnerDevice() {
        return ownerDevice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerDevice(String ownerDevice) {
        this.ownerDevice = ownerDevice;
    }
}
