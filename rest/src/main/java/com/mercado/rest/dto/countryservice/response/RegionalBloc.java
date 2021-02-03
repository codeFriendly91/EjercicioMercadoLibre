package com.mercado.rest.dto.countryservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegionalBloc {

    public String acronym;
    public String name;
    public List<String> otherAcronyms;
    public List<String> otherNames;

}
