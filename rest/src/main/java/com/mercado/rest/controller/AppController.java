package com.mercado.rest.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercado.rest.document.Rate;
import com.mercado.rest.dto.countryservice.response.Country;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import com.mercado.rest.dto.trace.response.TraceResponse;
import com.mercado.rest.repository.RateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;


@RestController
@RequestMapping("/")
public class AppController {

    Logger logger = LoggerFactory.getLogger(AppController.class);
    private RateRepository rateRepository;

    public AppController(RateRepository rateRepository){

        this.rateRepository = rateRepository;
    }


    @Value( "${rest.country.endpoint}" )
    private String countryEndpoint;

    @Value("${ip.country.endpoint}")
    private String ipEndPoint;

    @Value("${rest.currency.endpoint}")
    private String curencyEndpoint;

    @Value("${rest.currency.api.key}")
    private String curencyApiKey;



    @RequestMapping(value = "trace", method = RequestMethod.POST,  produces = { "application/json" })
    public TraceResponse trace(@RequestBody Map<String, String> body) throws JsonProcessingException {


        Country country;
        IpServiceResponse ipServiceResponse;
        TraceResponse traceResponse;

        try {

            RestTemplate restTemplate = new RestTemplate();
            StringBuilder sb = new StringBuilder();
            sb.append(ipEndPoint);
            sb.append(body.get("ip"));

            //execute ip API
            ipServiceResponse = restTemplate.getForObject(sb.toString(), IpServiceResponse.class);

            //execute country API
            country = restTemplate.getForObject(countryEndpoint+ipServiceResponse.getCountryCode3(), Country.class);

            traceResponse = mapResponse(body.get("ip"), country);

            Map<String, Object> currencyVariables = new HashMap<>();
            currencyVariables.put("apikey",curencyApiKey);

            //execute currency API
            String currencyResponse = restTemplate.getForObject(curencyEndpoint, String.class,currencyVariables);
            logger.info(currencyResponse);
            Map<String,Object> result =  new ObjectMapper().readValue(currencyResponse, HashMap.class);
            LinkedHashMap<Object,Object> rates =  (LinkedHashMap<Object, Object>) result.get("rates");

            rates.forEach((k,v) -> rateRepository.save(new Rate(LocalDateTime.now(),k.toString(),v.toString())));

            //String exchange = restTemplate.getForObject("http://data.fixer.io/api/latest?access_key=79df1eb4087e3af9ce531e4ac1cb36dd", String.class);


        }catch (Exception e) {

            logger.error("Ocurrio un error" , e);
            throw e;
        }
        return traceResponse;
    }



    @RequestMapping(value = "stats", method = RequestMethod.GET, produces = { "application/json" })
    public String stats(){

        return "hello";
    }


    @RequestMapping(value = "getall", method = RequestMethod.GET, produces = { "application/json" })
    public List<Rate> getAll(){

        return rateRepository.findAll();
    }


    @RequestMapping(value = "add", method = RequestMethod.GET, produces = { "application/json" })
    public void add(){

        LocalDateTime localDateTime = LocalDateTime.now();
        //rateRepository.save(new Rate(localDateTime,"",100.00));
        //rateRepository.save(new Rate(localDateTime,"",233.33));
    }





    public TraceResponse mapResponse(String ip, Country country){

        TraceResponse traceResponse = new TraceResponse();

        traceResponse.setIp(ip);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        traceResponse.setDate(dtf.format(now));
        traceResponse.setCountry(country.getName());
        traceResponse.setIso_code(country.getAlpha2Code());
        traceResponse.setLanguages(new ArrayList<>());
        country.getLanguages().forEach((language) -> traceResponse.getLanguages().add(language.getNativeName() + "(" + language.getIso639_2() + ")"));
        traceResponse.setTimes(new ArrayList<>());
        country.getTimezones().forEach((timeZone) -> traceResponse.getTimes().add(getCountryTime(timeZone)));
        traceResponse.setEstimated_distance(getDistanceToBsAs(country.getLatlng().get(0), country.getLatlng().get(1)));


        return traceResponse;
    }



    public String getCountryTime(String timeZone){

        String splitTimeZone = timeZone.split(":")[0];
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        if(timeZone.contains("-")) {

            int hourOnly = Integer.parseInt(splitTimeZone.replaceAll("[^0-9]", ""));
            Instant instant = Instant.now().minus(hourOnly, ChronoUnit.HOURS);
            date = Date.from(instant);

            return formatter.format(date) + " ("+ splitTimeZone +")";

        }else if(timeZone.contains("+")) {

            int hourOnly = Integer.parseInt(splitTimeZone.replaceAll("[^0-9]", ""));
            Instant instant = Instant.now().plus(hourOnly, ChronoUnit.HOURS);
            date = Date.from(instant);

            return formatter.format(date)+ " ("+ splitTimeZone +")";

        }else {

            Instant instant = Instant.now();
            date = Date.from(instant);
            return formatter.format(date) + " ("+ splitTimeZone +")";
        }


    }


    public String getDistanceToBsAs(int lat, int lng){


        final double degreesLatBsAs = Math.toRadians(-34);
        final double degreesLngBsAs = Math.toRadians(-64);
        //circunferencia de la tierra
        double earthCir = 40000;
        double dlat2 = Math.toRadians(lat);
        double dlng2 = Math.toRadians(lng);
        //obtengo el angulo entre los dos puntos y regla de 3
        //regla de 3. si para circunferencia 360 = 40000
        double distance = (Math.sin(degreesLatBsAs) * Math.sin(dlat2)) + (Math.cos(degreesLatBsAs) * Math.cos(dlat2) * Math.cos(degreesLngBsAs-dlng2));

        return Math.round((Math.toDegrees(Math.acos(distance)) / 360) * earthCir) +" kms";


    }

}
