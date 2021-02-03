package com.mercado.rest.dto.countryservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Language {

    public String iso639_1;
    public String iso639_2;
    public String name;
    public String nativeName;
}
