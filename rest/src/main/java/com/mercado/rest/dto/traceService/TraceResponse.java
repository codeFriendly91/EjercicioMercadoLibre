package com.mercado.rest.dto.traceService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
@EqualsAndHashCode
@Builder
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
    @JsonProperty("estimated_distance(Km)")
    private long estimatedDistance;
    @JsonIgnore
    private List<String> timeZones;
    @JsonIgnore
    String currencyCode;


    public TraceResponse(String ip, String date, String country, String isoCode, List<String> languages, String currency, List<String> times, long estimated_distance) {
        this.ip = ip;
        this.date = date;
        this.country = country;
        this.isoCode = isoCode;
        this.languages = languages;
        this.currency = currency;
        this.times = times;
        this.estimatedDistance = estimated_distance;
    }
}
