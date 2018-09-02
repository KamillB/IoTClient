package com.example.kamil.smartrpi.models.messages;

public class PeripheryModel {
    private String owner;
    private Long milis;
    private String name;
    private Integer gpioBcm;
    private Integer status;

    public PeripheryModel(){
    }

    public PeripheryModel(String owner, Long milis, String name, Integer gpioBcm, Integer status) {
        this.owner = owner;
        this.milis = milis;
        this.name = name;
        this.gpioBcm = gpioBcm;
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public Long getMilis() {
        return milis;
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getGpioBcm() {
        return gpioBcm;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setMilis(Long milis) {
        this.milis = milis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setGpioBcm(Integer gpioBcm) {
        this.gpioBcm = gpioBcm;
    }
}
