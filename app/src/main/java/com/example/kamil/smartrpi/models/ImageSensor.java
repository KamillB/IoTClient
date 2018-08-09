package com.example.kamil.smartrpi.models;

public class ImageSensor extends Sensor {
    private byte[] image;
    private Long milis;

    public ImageSensor(byte[] image, Long milis) {
        this.image = image;
        this.milis = milis;
    }

    public ImageSensor(String name, String ownerDevice, byte[] image, Long milis) {
        super(name, ownerDevice);
        this.image = image;
        this.milis = milis;
    }

    public byte[] getImage() {
        return image;
    }

    public Long getMilis() {
        return milis;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setMilis(Long milis) {
        this.milis = milis;
    }
}
