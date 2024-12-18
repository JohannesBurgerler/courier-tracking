package com.migrosonlinecase.couriertracking.model;

import java.time.LocalDateTime;
/**
 * Records when a courier enters a store.
 * Tracks courier ID, store name, and entry time.
 */
public class CourierStore {
    private int courierId;
    private String storeName;
    private LocalDateTime entranceDate;

    public CourierStore(int requestedCourierId, String requestedStoreName, LocalDateTime requestedEntranceDate) {
        courierId = requestedCourierId;
        storeName = requestedStoreName;
        entranceDate = requestedEntranceDate;
    }

    public int getCourierId(){
        return courierId;
    }
    public void setCourierId(int requestedCourierId){
        courierId = requestedCourierId;
    }

    public String getStoreName(){
        return storeName;
    }
    public void setStoreName(String requestedStoreName){
        storeName = requestedStoreName;
    }

    public LocalDateTime getEntranceDate(){
        return entranceDate;
    }
    public void setEntranceDate(LocalDateTime requestedEntranceDate){
        entranceDate = requestedEntranceDate;
    }
}
