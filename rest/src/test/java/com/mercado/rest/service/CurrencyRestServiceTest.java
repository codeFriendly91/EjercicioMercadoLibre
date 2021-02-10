package com.mercado.rest.service;

import com.mercado.rest.document.Rate;
import com.mercado.rest.repository.RateRepository;
import lombok.Builder;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyRestServiceTest {

    @Autowired
    CurrencyRestService currencyRestService;

    @MockBean
    RateRepository rateRepository;


    @Before
    public void setUp(){

        List<Rate> rateList = Arrays.asList(
                new Rate(LocalDateTime.now(),"AMD",633.377662),
                new Rate(LocalDateTime.now(),"AMD",633.377662),
                new Rate(LocalDateTime.now(),"AMD",633.377662),
                new Rate(LocalDateTime.now(),"AMD",633.377662)
        );

        when(rateRepository.findAll()).thenReturn(rateList);
    }


    @Test
    void get() throws Exception {

            currencyRestService.get();


    }
}