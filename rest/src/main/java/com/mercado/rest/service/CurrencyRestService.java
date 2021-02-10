package com.mercado.rest.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercado.rest.Exception.RestException;
import com.mercado.rest.document.Rate;
import com.mercado.rest.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyRestService {

    private final RateRepository rateRepository;

    @Value("${rest.currency.api.key}")
    private String curencyApiKey;

    @Value("${rest.currency.endpoint}")
    private String curencyEndpoint;

    public void get() throws RestException {

        //si el repo esta vacio ejecuto el servicio y cargo los datos

        if( rateRepository.findAll() == null || rateRepository.findAll().isEmpty()) {

            RestTemplate restTemplate = new RestTemplate();
            log.info("esta cargando desde el servicio los datos actuales de cambio en la base");
            Map<String, Object> currencyVariables = new HashMap<>();
            currencyVariables.put("apikey", curencyApiKey);
            ResponseEntity<String> currencyResponse = restTemplate.getForEntity(curencyEndpoint, String.class, currencyVariables);
            if(currencyResponse.getStatusCode().value() != 200){

                throw new RestException(currencyResponse.getStatusCodeValue());

            }
            Map<String, Object> result = null;
            try {
                result = new ObjectMapper().readValue(currencyResponse.getBody(), HashMap.class);
            } catch (JsonProcessingException e) {
                throw new RestException(500,e.getCause().getMessage());
            }
            LinkedHashMap<Object, Object> rates = (LinkedHashMap<Object, Object>) result.get("rates");
            LocalDateTime localDateTime = LocalDateTime.now();
            rates.entrySet().stream().forEach(e -> rateRepository.save(new Rate(localDateTime, e.getKey().toString(), Double.parseDouble(e.getValue().toString()))));
        }

    }

}
