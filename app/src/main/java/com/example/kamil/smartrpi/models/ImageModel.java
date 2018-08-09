package com.example.kamil.smartrpi.models;

public class ImageModel {
    private String owner;
    private String image;
    private Long milis;
    private String name;

    public ImageModel(){
    }
    public ImageModel(String owner, String image, Long milis, String name) {
        this.owner = owner;
        this.image = image;
        this.milis = milis;
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage() {
        return image;
    }

    public Long getMilis() {
        return milis;
    }

    public String getName() {
        return name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMilis(Long milis) {
        this.milis = milis;
    }

    public void setName(String name) {
        this.name = name;
    }
}
