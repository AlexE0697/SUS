package com.example.sus.Activities.Models;

public class LocationModel {

    private String title;
    private Double location_lat;
    private Double location_lon;

    public LocationModel() {
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public Double getlocation_lat() {
        return location_lat;
    }

    public void setlocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }

    public Double getlocation_lon() {
        return location_lon;
    }

    public void setlocation_lon(Double location_lon) {
        this.location_lon = location_lon;
    }
}
