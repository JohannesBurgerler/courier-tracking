package com.migrosonlinecase.couriertracking.exception.technical;

import com.migrosonlinecase.couriertracking.exception.base.CourierTrackingException;

public class DataAccessException extends CourierTrackingException {
    public DataAccessException(String operation, String entity, Throwable cause) {
        super("Data access error during " + operation + " for " + entity, "DATA_ACCESS_ERROR", cause);
    }
}
