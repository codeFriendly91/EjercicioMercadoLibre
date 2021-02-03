package com.mercado.rest.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mercado.rest.dto.countryservice.response.Country;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import com.mercado.rest.dto.trace.response.TraceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.*;


@RestController
@RequestMapping
public class AppController {

    Logger logger = LoggerFactory.getLogger(AppController.class);


    @Value( "${rest.country.endpoint}" )
    private String countryEndpoint;

    @Value("${ip.country.endpoint}")
    private String ipEndPoint;


    @RequestMapping(value = "trace", method = RequestMethod.POST,  produces = { "application/json" })
    public IpServiceResponse trace(@RequestBody Map<String, String> body){


        Country country = new Country();
        IpServiceResponse ipServiceResponse = new IpServiceResponse();

        try {

            RestTemplate restTemplate = new RestTemplate();
            StringBuilder sb = new StringBuilder();
            sb.append(ipEndPoint);
            sb.append("ip?");
            sb.append(body.get("ip"));

            //execute ip API
            ipServiceResponse = restTemplate.getForObject(sb.toString(), IpServiceResponse.class);

            //execute country API
            country = restTemplate.getForObject(countryEndpoint+ipServiceResponse.getCountryCode3(), Country.class);

            //String exchange = restTemplate.getForObject("http://data.fixer.io/api/latest?access_key=79df1eb4087e3af9ce531e4ac1cb36dd", String.class);




        }catch (Exception e) {

            logger.error("Ocurrio un error" , e);
            throw e;
        }
        return ipServiceResponse;
    }



    @RequestMapping(value = "stats", method = RequestMethod.GET, produces = { "application/json" })
    public String stats(){

        return "";
    }


    public TraceResponse mapResponse(String ip, Country country){

        TraceResponse traceResponse = new TraceResponse();

        traceResponse.setIp(ip);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        traceResponse.setDate(dtf.format(now));
        traceResponse.setCountry(country.getName());
        traceResponse.setIso_code(country.getAlpha2Code());
        country.getLanguages().forEach((p) -> traceResponse.getLanguages().add(p.getNativeName() + "(" + p.getIso639_2()));

        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.getInstance();

        return null;
    }

}
