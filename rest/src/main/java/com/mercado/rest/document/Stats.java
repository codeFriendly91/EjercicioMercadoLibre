package com.mercado.rest.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Stats {

    @JsonIgnore
    @Id
    String id;
    private String country;
    private long distance;
    private int invocations;

    public Stats(String country, long distance, int invocations) {
        this.country = country;
        this.distance = distance;
        this.invocations = invocations;
    }
}
