package com.mercado.rest.service;

import com.mercado.rest.controller.StatsController;
import com.mercado.rest.document.Stats;
import com.mercado.rest.dto.statsservice.StatsServiceResponse;
import com.mercado.rest.repository.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class StatsServiceTest {

    @MockBean
    StatsRepository statsRepository;

    @Autowired
    StatsService statsService;

    private List<Stats> stats;

    @BeforeEach
    public void setUp(){


        Stats argentinaStats =new Stats("Argentina",0,10);
        Stats anguillaStats =new Stats("Anguilla",5779,10);
        Stats gabonStats =new Stats("Gabon",8560,3);
        stats = Arrays.asList(argentinaStats,anguillaStats,gabonStats);
        when(statsRepository.findTopByOrderByDistanceAsc()).thenReturn(gabonStats);
        when(statsRepository.findTopByOrderByDistanceDesc()).thenReturn(argentinaStats);
        when(statsRepository.findAll()).thenReturn(stats);

    }


    @Test
    void getMaxAndMinStats() {


       StatsServiceResponse statsServiceResponse = statsService.getMaxAndMinStats();
        System.out.println(statsServiceResponse.getStatsList());
       //averageDistance
        assertEquals(3629,statsServiceResponse.getAverageDistance());
       //MaxStat
       assertEquals("Gabon",statsServiceResponse.getStatsList().get(0).getCountry());
        //MinStat
       assertEquals("Argentina",statsServiceResponse.getStatsList().get(1).getCountry());

    }


    @Test
    void getAverageDistance() {

        assertEquals(3629, statsService.getAverageDistance(stats));

    }
}