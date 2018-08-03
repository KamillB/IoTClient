package com.example.kamil.smartrpi.models;

import java.util.List;

public class Payload {
    private List<TemperatureModel> temperatureModel;
    private List<ImageModel> imageModel;
    private String deviceKey;

    public Payload(){
    }

    public Payload(List<TemperatureModel> temperatureModel, List<ImageModel> imageModel, String deviceKey) {
        this.temperatureModel = temperatureModel;
        this.imageModel = imageModel;
        this.deviceKey = deviceKey;
    }

    public List<TemperatureModel> getTemperatureModel() { return temperatureModel; }

    public List<ImageModel> getImageModel() { return imageModel; }

    public String getDeviceKey() { return deviceKey; }

    public void setTemperatureModel(List<TemperatureModel> temperatureModel) { this.temperatureModel = temperatureModel; }

    public void setImageModel(List<ImageModel> imageModel) { this.imageModel = imageModel; }

    public void setDeviceKey(String deviceKey) { this.deviceKey = deviceKey; }

    public void addTemperatureModel(TemperatureModel singleTemperature){ this.temperatureModel.add(singleTemperature); }

    public void addImageModel(ImageModel singleImage){ this.imageModel.add(singleImage); }
}
