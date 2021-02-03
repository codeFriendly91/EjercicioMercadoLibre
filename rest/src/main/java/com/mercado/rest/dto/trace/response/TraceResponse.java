package com.mercado.rest.dto.trace.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TraceResponse {

    public String ip;
    public String date;
    public String country;
    public String iso_code;
    public List<String> languages;
    public String currency;
    public List<String> times;
    public String estimated_distance;



}
