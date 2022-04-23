package com.qbros.uithymeleaf.services;

/**
 * Type of the exceptions thrown by the Service layer.
 */
public class ServiceLayerException extends RuntimeException {

    public ServiceLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
