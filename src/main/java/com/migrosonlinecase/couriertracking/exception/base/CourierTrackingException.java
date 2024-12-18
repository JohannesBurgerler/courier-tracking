package com.migrosonlinecase.couriertracking.exception.base;

public class CourierTrackingException extends RuntimeException{
    private final String errorCode;

    public CourierTrackingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CourierTrackingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
