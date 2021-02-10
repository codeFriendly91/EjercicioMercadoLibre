package com.mercado.rest.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RestException extends Exception {

    private int statusCode;


    public RestException(int statusCode, String msj){
        super(msj);
        this.statusCode = statusCode;

    }
}
