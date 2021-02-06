package com.mercado.rest.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Rate {


    @Indexed(expireAfter = "12h")
    private LocalDateTime createdAt;
    private String currency;
    private double value;


}
