package com.mercado.rest.controller;


import com.mercado.rest.dto.statsservice.StatsServiceResponse;
import com.mercado.rest.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @RequestMapping(value = "stats", method = RequestMethod.GET, produces = { "application/json" })
    public StatsServiceResponse stats (){

        return statsService.getMaxAndMinStats();
    }
}
