package com.example.kamil.smartrpi.models.messages;

public class TemperatureModel {
    private String ownerSerialNumber;
    private Double temp;
    private Long milis;
    private String name;

    TemperatureModel(){
    }

    public TemperatureModel(String ownerSerialNumber, Double temp, Long milis, String name) {
        this.ownerSerialNumber = ownerSerialNumber;
        this.temp = temp;
        this.milis = milis;
        this.name = name;
    }

    public String getOwnerSerialNumber() { return ownerSerialNumber; }

    public Double getTemp() { return temp; }

    public Long getMilis() { return milis; }

    public String getName() { return name; }

    public void setOwnerSerialNumber(String ownerSerialNumber) { this.ownerSerialNumber = ownerSerialNumber; }

    public void setTemp(Double temp) { this.temp = temp; }

    public void setMilis(Long milis) { this.milis = milis; }

    public void setName(String name) { this.name = name; }
}
