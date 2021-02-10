package com.mercado.rest.service;

import com.mercado.rest.Exception.RestException;
import com.mercado.rest.dto.countryservice.response.Country;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CountryRestServiceTest {

    @Autowired
    CountryRestService countryRestService;


    @Test
    void get() throws RestException {
        Country country = countryRestService.get("AR");
        assertEquals("Argentina",country.getName());
        assertEquals("ARG", country.getAlpha3Code());
    }

    @Test
    void getBadRequest(){

        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            //paso como parametro un pais que no existe
            countryRestService.get("dummy");
        });
    }
}