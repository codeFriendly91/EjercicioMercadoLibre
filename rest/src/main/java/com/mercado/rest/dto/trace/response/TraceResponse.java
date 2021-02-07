package com.mercado.rest.dto.trace.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Id
    String id;
    private String ip;
    private String date;
    private String country;
    private String isoCode;
    private List<String> languages;
    private String currency;
    private List<String> times;
    private long estimated_distance;


    public TraceResponse(String ip, String date, String country, String isoCode, List<String> languages, String currency, List<String> times, long estimated_distance) {
        this.ip = ip;
        this.date = date;
        this.country = country;
        this.isoCode = isoCode;
        this.languages = languages;
        this.currency = currency;
        this.times = times;
        this.estimated_distance = estimated_distance;
    }
}
