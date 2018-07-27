package com.example.kamil.smartrpi.models;

public class Payload {
    private TemperatureModel temperatureModel;
    private ImageModel imageModel;
    private String deviceKey;

    public Payload(){
    }

    public Payload(TemperatureModel temperatureModel, ImageModel imageModel, String deviceKey) {
        this.temperatureModel = temperatureModel;
        this.imageModel = imageModel;
        this.deviceKey = deviceKey;
    }

    public TemperatureModel getTemperatureModel() { return temperatureModel; }

    public ImageModel getImageModel() { return imageModel; }

    public String getDeviceKey() { return deviceKey; }

    public void setTemperatureModel(TemperatureModel temperatureModel) { this.temperatureModel = temperatureModel; }

    public void setImageModel(ImageModel imageModel) { this.imageModel = imageModel; }

    public void setDeviceKey(String deviceKey) { this.deviceKey = deviceKey; }
}
