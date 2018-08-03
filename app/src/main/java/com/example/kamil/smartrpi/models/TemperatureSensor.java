package com.example.kamil.smartrpi.models;

public class TemperatureSensor extends Sensor {
    private Double temp;
    private Long milis;

    TemperatureSensor(){
    }

    public TemperatureSensor(Double temp, Long milis) {
        this.temp = temp;
        this.milis = milis;
    }

    public TemperatureSensor(String name, String ownerDevice, Double temp, Long milis) {
        super(name, ownerDevice);
        this.temp = temp;
        this.milis = milis;
    }

    public Double getTemp() { return temp; }

    public Long getMilis() { return milis; }

    public void setTemp(Double temp) { this.temp = temp; }

    public void setMilis(Long milis) { this.milis = milis; }

}