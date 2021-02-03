package com.mercado.rest.dto.countryservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Country {

    public String name;
    public List<String> topLevelDomain;
    public String alpha2Code;
    public String alpha3Code;
    public List<String> callingCodes;
    public String capital;
    public List<String> altSpellings;
    public String region;
    public String subregion;
    public int population;
    public List<Integer> latlng;
    public String demonym;
    public int area;
    public double gini;
    public List<String> timezones;
    public List<String> borders;
    public String nativeName;
    public String numericCode;
    public List<Currency> currencies;
    public List<Language> languages;
    public Translations translations;
    public String flag;
    public List<RegionalBloc> regionalBlocs;
    public String cioc;

}
