package com.migrosonlinecase.couriertracking.exception.business;

import com.migrosonlinecase.couriertracking.exception.base.CourierTrackingException;

public class CourierNotFoundException extends CourierTrackingException {
    public CourierNotFoundException(int courierId) {
        super("Courier not found with ID: " + courierId, "COURIER_NOT_FOUND");
    }
}
