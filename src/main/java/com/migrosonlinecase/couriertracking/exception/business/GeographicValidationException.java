package com.migrosonlinecase.couriertracking.exception.business;

import com.migrosonlinecase.couriertracking.exception.base.CourierTrackingException;

public class GeographicValidationException extends CourierTrackingException {
    public GeographicValidationException(String message) {
        super(message, "GEO_VALIDATION_ERROR");
    }
}
