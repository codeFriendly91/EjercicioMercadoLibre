package com.mercado.rest.controller;

import com.google.gson.Gson;
import com.mercado.rest.dto.traceService.TraceRequest;
import com.mercado.rest.dto.traceService.TraceResponse;
import com.mercado.rest.service.TraceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = TraceController.class)
class TraceControllerTest {

    @MockBean
    TraceService traceService;

    @Autowired
    MockMvc mockmvc;

    TraceResponse traceResponse;

    @BeforeEach
    public void setUp(){

     String json = "{\n" +
                "   \"ip\": \"45.5.15.0\",\n" +
                "   \"date\": \"09/02/2021 04:17:31\",\n" +
                "   \"country\": \"Argentina\",\n" +
                "   \"isoCode\": \"AR\",\n" +
                "   \"languages\":    [\n" +
                "      \"Spanish(spa)\",\n" +
                "      \"Guarani\u00AD(grn)\"\n" +
                "   ],\n" +
                "   \"currency\": \"1 EUR = 106.324148 ARS\",\n" +
                "   \"times\": [\"04:17:32 (UTC-03)\"],\n" +
                "   \"estimated_distance(Km)\": 0\n" +
                "}";
        traceResponse = new Gson().fromJson(json, TraceResponse.class);
    }



    @Test
    void trace() throws Exception {

        Mockito.when(traceService.trace(new TraceRequest("45.5.15.0"))).thenReturn(traceResponse);
        mockmvc.perform(MockMvcRequestBuilders.post("/trace").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +"\t\"ip\": \"45.5.15.0\"\n" +"}"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date", Matchers.is("09/02/2021 04:17:31")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country", Matchers.is("Argentina")));

    }




    @Test
    void traceFailure() throws Exception {

        Mockito.when(traceService.trace(new TraceRequest("45.5.15.0"))).thenReturn(traceResponse);
        mockmvc.perform(MockMvcRequestBuilders.post("/trace").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().is(500));
    }

}