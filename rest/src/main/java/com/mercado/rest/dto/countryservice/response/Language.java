package com.mercado.rest.dto.countryservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Language {

    private String iso639_1;
    private String iso639_2;
    private String name;
    private String nativeName;
}
