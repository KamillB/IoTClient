package com.example.kamil.smartrpi.models.data;

public class PeripherySensor extends Sensor {
    private Long milis;
    private Integer gpioBcm;
    private Integer status;

    PeripherySensor(){
    }

    public PeripherySensor(Long milis, Integer gpioBcm, Integer status) {
        this.milis = milis;
        this.gpioBcm = gpioBcm;
        this.status = status;
    }

    public PeripherySensor(String name, String ownerDevice, Long milis, Integer gpioBcm, Integer status) {
        super(name, ownerDevice);
        this.milis = milis;
        this.gpioBcm = gpioBcm;
        this.status = status;
    }

    public Long getMilis() {
        return milis;
    }

    public Integer getGpioBcm() {
        return gpioBcm;
    }

    public Integer getStatus() {
        return status;
    }

    public void setMilis(Long milis) {
        this.milis = milis;
    }

    public void setGpioBcm(Integer gpioBcm) {
        this.gpioBcm = gpioBcm;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
