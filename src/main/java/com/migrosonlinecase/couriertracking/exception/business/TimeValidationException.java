package com.migrosonlinecase.couriertracking.exception.business;

import com.migrosonlinecase.couriertracking.exception.base.CourierTrackingException;

public class TimeValidationException extends CourierTrackingException {
  public TimeValidationException(String message) {
    super(message, "TIME_VALIDATION_ERROR");
  }
}
