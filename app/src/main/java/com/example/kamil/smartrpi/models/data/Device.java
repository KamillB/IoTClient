package com.example.kamil.smartrpi.models.data;

import com.example.kamil.smartrpi.models.data.Sensor;

import java.util.List;

public class Device {
    private String mac;
    private List<Sensor> sensorList;

    Device(){
    }

    public Device(String mac, List<Sensor> sensorList) {
        this.mac = mac;
        this.sensorList = sensorList;
    }


    public String getMac() {
        return mac;
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }
}
