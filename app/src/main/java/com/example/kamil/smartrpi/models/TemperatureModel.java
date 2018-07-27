package com.example.kamil.smartrpi.models;

public class TemperatureModel {
    private String owner;
    private Double temp;
    private Long milis;
    private String name;

    TemperatureModel(){
    }

    public TemperatureModel(String owner, Double temp, Long milis, String name) {
        this.owner = owner;
        this.temp = temp;
        this.milis = milis;
        this.name = name;
    }

    public String getOwner() { return owner; }

    public Double getTemp() { return temp; }

    public Long getMilis() { return milis; }

    public String getName() { return name; }

    public void setOwner(String owner) { this.owner = owner; }

    public void setTemp(Double temp) { this.temp = temp; }

    public void setMilis(Long milis) { this.milis = milis; }

    public void setName(String name) { this.name = name; }
}
