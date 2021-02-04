package com.mercado.rest.controller;

import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AppController appController;


    @Test
    void trace() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/trace").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "\t\"ip\": \"5.6.7.8\"\n" +
                "}");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200,result.getResponse().getStatus());


    }

    @Test
    void stats() {



    }

    @Test
    void mapResponse() {


    }

    @Test
    void getCountryTime() {

        System.out.println(appController.getCountryTime("UTC+01"));
    }

    @Test
    void getDistanceToBsAs() {

        //distancia aproximada a alemania
        assertEquals("11820 kms",appController.getDistanceToBsAs(51,9));

    }
}