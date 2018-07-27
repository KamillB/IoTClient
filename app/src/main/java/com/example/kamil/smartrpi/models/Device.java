package com.example.kamil.smartrpi.models;

import java.util.List;

public class Device {
    private List<TemperatureModel> temperatureList;
    private List<ImageModel> cameraList;

    Device(){
    }

    public Device(List<TemperatureModel> temperatureList, List<ImageModel> cameraList) {
        this.temperatureList = temperatureList;
        this.cameraList = cameraList;
    }

    public List<TemperatureModel> getTemperatureList() { return temperatureList; }

    public List<ImageModel> getCameraList() { return cameraList; }

    public void setTemperatureList(List<TemperatureModel> temperatureList) { this.temperatureList = temperatureList; }

    public void setCameraList(List<ImageModel> cameraList) { this.cameraList = cameraList; }
}
