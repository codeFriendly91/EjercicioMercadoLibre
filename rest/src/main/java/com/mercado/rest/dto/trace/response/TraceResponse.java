package com.mercado.rest.dto.trace.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TraceResponse {

    private String ip;
    private String date;
    private String country;
    private String iso_code;
    private List<String> languages;
    private String currency;
    private List<String> times;
    private String estimated_distance;



}
