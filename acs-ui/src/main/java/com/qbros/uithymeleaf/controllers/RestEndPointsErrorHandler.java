package com.qbros.uithymeleaf.controllers;

import com.qbros.acs.api.sharedmodels.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/*
 * this error handler is added for the few remaining endpoints that are called directly via js
 */
@ControllerAdvice
public class RestEndPointsErrorHandler {

    //fixme improve response codes based on exception type.
    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        ApiError body = new ApiError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
