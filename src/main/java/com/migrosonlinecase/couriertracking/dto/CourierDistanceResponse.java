package com.migrosonlinecase.couriertracking.dto;

/**
 * Response object for courier's total travel distance.
 * Distance is always provided in kilometers.
 */
public class CourierDistanceResponse {
    private int courierId;
    private double totalDistance;
    private String unit = "kilometers";

    public CourierDistanceResponse(int courierId, double totalDistance) {
        this.courierId = courierId;
        this.totalDistance = totalDistance;
        this.unit = "kilometers";
    }

    public int getCourierId() {
        return courierId;
    }
    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }
    public double getTotalDistance() {
        return totalDistance;
    }
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
