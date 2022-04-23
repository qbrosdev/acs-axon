package com.qbros.acs.api.sharedmodels.api;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ApiError {

    LocalDateTime timestamp;
    String message;

    public ApiError(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ApiError(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
