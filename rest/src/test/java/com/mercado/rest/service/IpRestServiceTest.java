package com.mercado.rest.service;

import com.mercado.rest.Exception.RestException;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IpRestServiceTest {

    @Autowired
    IpRestService restService;

    @Test
    void get() throws RestException {

        //direccion ip Argentina
        IpServiceResponse  ipServiceResponse=restService.get("45.5.15.0");
        assertEquals("Argentina", ipServiceResponse.getCountryName());
        assertEquals("ARG", ipServiceResponse.getCountryCode3());

    }

    @Test
    void getBadRequest() {

        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            //paso como parametro una ip mal formada
            restService.get("45.5.15");
        });
    }
}