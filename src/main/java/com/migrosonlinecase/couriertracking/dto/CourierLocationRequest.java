package com.migrosonlinecase.couriertracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 * Request object for recording courier location.
 * Includes courier ID, coordinates, and timestamp.
 */
public class CourierLocationRequest {
    private int courierId;
    private double latitude;
    private double longitude;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    public CourierLocationRequest() {}

    public CourierLocationRequest(int requestedCourierId, double requestedLat, double requestedLng, LocalDateTime requestedTime){
        courierId = requestedCourierId;
        latitude = requestedLat;
        longitude = requestedLng;
        time = requestedTime;
    }

    public int getCourierId() {
        return courierId;
    }
    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public LocalDateTime getTime(){
        return time;
    }
    public void setTime(LocalDateTime requestedTime){
        time = requestedTime;
    }
}
