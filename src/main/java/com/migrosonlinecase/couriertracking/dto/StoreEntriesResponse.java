package com.migrosonlinecase.couriertracking.dto;

import java.time.LocalDateTime;

/**
 * Response object for store entry history.
 * Contains when and which store a courier entered.
 */
public class StoreEntriesResponse {
    private int courierId;
    private String storeName;
    private LocalDateTime entranceDate;

    public StoreEntriesResponse(int requestedCourierId, String requestedStoreName, LocalDateTime requestedEntranceDate) {
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
