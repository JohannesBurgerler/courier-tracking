package com.migrosonlinecase.couriertracking.exception.business;

import com.migrosonlinecase.couriertracking.exception.base.CourierTrackingException;

public class StoreNotFoundException extends CourierTrackingException {
    public StoreNotFoundException(String storeName) {
        super("Store not found: " + storeName, "STORE_NOT_FOUND");
    }
}
