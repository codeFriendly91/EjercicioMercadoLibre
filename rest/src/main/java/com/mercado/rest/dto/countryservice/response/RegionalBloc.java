package com.mercado.rest.dto.countryservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegionalBloc {

    private String acronym;
    private String name;
    private List<String> otherAcronyms;
    private List<String> otherNames;

}
