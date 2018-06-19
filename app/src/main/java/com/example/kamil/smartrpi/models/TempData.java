package com.example.kamil.smartrpi.models;

public class TempData {
    private Integer id;
    private String name;
    private Float temp;

    public TempData(){
    }

    public TempData(Integer id, String name, Float temp) {
        this.id = id;
        this.name = name;
        this.temp = temp;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Integer getId() { return id; }

    public String getName() {
        return name;
    }

    public Float getTemp() {
        return temp;
    }
}
