package com.mercado.rest.dto.trace.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class TraceResponse {


    private String ip;
    private String date;
    private String country;
    private String isoCode;
    private List<String> languages;
    private String currency;
    private List<String> times;
    private String estimated_distance;



}
