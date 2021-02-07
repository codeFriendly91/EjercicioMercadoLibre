package com.mercado.rest.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpInfo {

    @JsonIgnore
    @Id
    private String ip;
    private String date;
    private String country;
    private String isoCode;
    private List<String> languages;
    private String currency;
    private List<String> times;
    private String estimated_distance;

}
