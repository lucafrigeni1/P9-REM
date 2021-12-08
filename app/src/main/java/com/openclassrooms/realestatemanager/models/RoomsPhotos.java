package com.openclassrooms.realestatemanager.models;

public class RoomsPhotos {

    String url;
    String description;

    public RoomsPhotos() {
    }

    public RoomsPhotos(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
