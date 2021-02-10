package com.mercado.rest.document;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
@EqualsAndHashCode
public class Rate {

    @JsonIgnore
    @Id
    String id;
    @Indexed(expireAfter = "12h")
    private LocalDateTime createdAt;
    private String currency;
    private double value;



    public Rate(LocalDateTime createdAt, String currency, double value) {
        this.createdAt = createdAt;
        this.currency = currency;
        this.value = value;
    }
}
