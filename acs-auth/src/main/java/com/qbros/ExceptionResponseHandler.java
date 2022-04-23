package com.qbros;

import com.qbros.acs.api.sharedmodels.api.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleExceptions(Exception ex, WebRequest request) {
        log.info("Handling exception {}", ex, ex);
        return new ResponseEntity<>(new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
