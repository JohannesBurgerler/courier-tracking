package com.migrosonlinecase.couriertracking.model;

import java.time.LocalDateTime;
/**
 * Represents a courier with their location and timestamp.
 */
public class Courier {

    private int id;
    private double lat;
    private double lng;
    private LocalDateTime time;

    public Courier(int requestedCourierId, double requestedLat, double requestedLng, LocalDateTime requestedTime){
        id = requestedCourierId;
        lat = requestedLat;
        lng = requestedLng;
        time = requestedTime;
    }

    public int getId(){
        return id;
    }
    public void setId(int requestedId){
        id = requestedId;
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

    public LocalDateTime getTime(){
        return time;
    }
    public void setTime(LocalDateTime requestedTime){
        time = requestedTime;
    }
}
