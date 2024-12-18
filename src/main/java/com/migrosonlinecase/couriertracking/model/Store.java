package com.migrosonlinecase.couriertracking.model;
/**
 * Represents a store location with its coordinates.
 */
public class Store {
    private String name;
    private double lat;
    private double lng;

    public String getName(){
        return name;
    }

    public double getLat(){
        return lat;
    }
    public void setLat(double requestedLat){
        lat = requestedLat;
    }

    public double getLng(){
        return lng;
    }
    public void setLng(double requestedLng){
        lng = requestedLng;
    }

    public void setName(String requestedName) {
        name = requestedName;
    }
}
