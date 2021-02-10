package com.mercado.rest.service;

import com.mercado.rest.Exception.RestException;
import com.mercado.rest.dto.countryservice.response.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CountryRestService {

    @Value( "${rest.country.endpoint}" )
    private String countryEndpoint;

    public Country get(String isoCode){

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Country> countryServiceResponse = restTemplate.exchange(countryEndpoint + isoCode, HttpMethod.GET, null, Country.class);

        if(countryServiceResponse.getStatusCode().value() != 200){

            throw new RestException(countryServiceResponse.getStatusCode().value());

        }
        return countryServiceResponse.getBody();
    }


}
