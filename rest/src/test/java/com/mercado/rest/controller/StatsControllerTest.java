package com.mercado.rest.controller;

import com.mercado.rest.document.Stats;
import com.mercado.rest.dto.statsservice.StatsServiceResponse;
import com.mercado.rest.repository.StatsRepository;
import com.mercado.rest.service.StatsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;


@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {

    @MockBean
    StatsService statsService;

    @Autowired
    MockMvc mockMvc;


    @Test
    void stats() throws Exception {

        StatsServiceResponse serviceResponse = new StatsServiceResponse();
        serviceResponse.setAverageDistance(1000L);
        serviceResponse.setStatsList(Arrays.asList(new Stats("Argentina",0,1),new Stats("Argentina",1000L,1)));
        Mockito.when(statsService.getMaxAndMinStats()).thenReturn(serviceResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/stats"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }


}