package com.mercado.rest.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.Date;


@ControllerAdvice
public class IpServiceExceptionHandler {

    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<ExceptionResponse> handleIpServiceException(RestException restException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), restException.getStatusCode(),restException.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(restException.getStatusCode()));

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionResponse> handleIpServiceException(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage());
        return new ResponseEntity <>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
