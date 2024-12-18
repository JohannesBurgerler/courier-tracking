package com.migrosonlinecase.couriertracking.dto;

/**
 * Response object for courier location updates.
 * Indicates if the courier entered a store and which one.
 */
public class CourierLocationResponse {
    private boolean storeEntranceRecorded;
    private String storeName;  // null if no entrance recorded
    private String message;

    public CourierLocationResponse(boolean storeEntranceRecorded, String storeName) {
        this.storeEntranceRecorded = storeEntranceRecorded;
        this.storeName = storeName;
        this.message = storeEntranceRecorded ?
                "Store entrance recorded for: " + storeName :
                "Location recorded, no store entrance detected";
    }

    // Getters
    public boolean isStoreEntranceRecorded() {
        return storeEntranceRecorded;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getMessage() {
        return message;
    }
}
