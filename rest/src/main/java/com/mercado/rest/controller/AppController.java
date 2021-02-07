package com.mercado.rest.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercado.rest.document.Rate;
import com.mercado.rest.document.Stats;
import com.mercado.rest.dto.countryservice.response.Country;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import com.mercado.rest.dto.trace.response.TraceResponse;
import com.mercado.rest.repository.RateRepository;
import com.mercado.rest.repository.StatsRepository;
import com.mercado.rest.repository.TraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RestController
@RequestMapping("/")
public class AppController {

    Logger logger = LoggerFactory.getLogger(AppController.class);

    private final RateRepository rateRepository;
    private final TraceRepository traceRepository;
    private final StatsRepository statsRepository;


    public AppController(RateRepository rateRepository, TraceRepository traceRepository, StatsRepository statsRepository ){
        this.traceRepository = traceRepository;
        this.rateRepository = rateRepository;
        this.statsRepository = statsRepository;
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
    public ResponseEntity trace(@RequestBody Map<String, String> body) throws JsonProcessingException {


        Country country;
        IpServiceResponse ipServiceResponse;
        TraceResponse traceResponse;
        Rate rate;
        Stats stats = new Stats();
        try {

            RestTemplate restTemplate = new RestTemplate();
            StringBuilder sb = new StringBuilder();
            sb.append(ipEndPoint);
            sb.append(body.get("ip"));
            //execute ip API
            ResponseEntity<IpServiceResponse> ipServiceResponseEntity = restTemplate.exchange(sb.toString(), HttpMethod.GET,null, IpServiceResponse.class);

            if(ipServiceResponseEntity.getStatusCode().value() != 200){
                //ver como hacer la exception
                return ipServiceResponseEntity;

            }

            ipServiceResponse = ipServiceResponseEntity.getBody();
            loadCurrencyOnDatabase(restTemplate);


            //Busco en la base si ya no tengo una respuesta anterior con la data
            //Si no devuelve nada ejecuto el servicio

            if(traceRepository.findByIsoCode(ipServiceResponse.getCountryCode()) == null) {
                logger.info("ejecutando el servicio de pais");

                //execute country API
                ResponseEntity<Country> countryServiceResponseEntity = restTemplate.exchange(countryEndpoint+ipServiceResponse.getCountryCode3(), HttpMethod.GET,null, Country.class);


                if(countryServiceResponseEntity.getStatusCode().value() != 200){

                    return countryServiceResponseEntity;

                }

                country = countryServiceResponseEntity.getBody();
                rate = rateRepository.findByCurrency(country.getCurrencies().get(0).getCode());
                //Si no estan los datos del cambio en la base de datos los busco del servicio
                traceResponse = mapResponse(body.get("ip"), country, rate);
                //execute currency API
                traceRepository.save(traceResponse);

            }else{

                logger.info("lo esta levantando de la base, pero hay que agregarle el cambio y la hora actual");
                //TODO:agregarle el cambio y la hora actual
                traceResponse = traceRepository.findByIsoCode(ipServiceResponse.getCountryCode());


            }

        }catch (Exception e) {

            logger.error("Ocurrio un error" , e);
            throw e;
        }
        saveStats(traceResponse);
        return new ResponseEntity(traceResponse, HttpStatus.OK);

    }



    @RequestMapping(value = "stats", method = RequestMethod.GET, produces = { "application/json" })
    public List<Stats> stats(){

        List<Stats> statsList = new ArrayList<>();
        Stats maxStats = statsRepository.findTopByOrderByDistanceAsc();
        Stats minStats = statsRepository.findTopByOrderByDistanceDesc();

        if(maxStats != null){
            statsList.add(maxStats);
        }

        if(minStats != null){
            statsList.add(minStats);
        }


        return statsList;
    }


    @RequestMapping(value = "getall", method = RequestMethod.GET, produces = { "application/json" })
    public List<TraceResponse> getAll(){
        if(traceRepository.findByIsoCode("ESP") == null) {

            logger.info("NO VINIERON DATOS DE LA QUERY");
        }

        return traceRepository.findAll();

    }


    @RequestMapping(value = "add", method = RequestMethod.GET, produces = { "application/json" })
    public void add(){


    }


    /**
     * Mapea la respuesta final
     * @param ip
     * @param country
     * @param rate
     * @return
     */

    public TraceResponse mapResponse(String ip, Country country, Rate rate){

        TraceResponse traceResponse = new TraceResponse();

        traceResponse.setIp(ip);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        traceResponse.setDate(dtf.format(now));
        traceResponse.setCountry(country.getName());
        traceResponse.setIsoCode(country.getAlpha2Code());
        traceResponse.setLanguages(new ArrayList<>());
        country.getLanguages().stream().forEach((language) -> traceResponse.getLanguages().add(language.getName() + "(" + language.getIso639_2() + ")"));
        traceResponse.setTimes(new ArrayList<>());
        country.getTimezones().stream().forEach((timeZone) -> traceResponse.getTimes().add(getCountryTime(timeZone)));
        traceResponse.setEstimated_distance(getDistanceToBsAs(country.getLatlng().get(0), country.getLatlng().get(1)));
        traceResponse.setCurrency("1 EUR = " + rate.getValue() +" " + country.getCurrencies().get(0).getCode());


        return traceResponse;
    }




    /**
     * Calcula la hora actual de un pais a partir del time-zone pasado como parametro
     * @param timeZone
     * @return
     */
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


    /**
     * Calcula la distancia desde Buenos Aires hasta la lat y lng pasadas como parametros
     * @param lat
     * @param lng
     * @return
     */

    public long getDistanceToBsAs(int lat, int lng){

        final double degreesLatBsAs = Math.toRadians(-34);
        final double degreesLngBsAs = Math.toRadians(-64);
        //circunferencia de la tierra
        double earthCir = 40000;
        double dlat2 = Math.toRadians(lat);
        double dlng2 = Math.toRadians(lng);
        //obtengo el angulo entre los dos puntos y regla de 3
        //regla de 3. si para circunferencia 360 = 40000
        double distance = (Math.sin(degreesLatBsAs) * Math.sin(dlat2)) + (Math.cos(degreesLatBsAs) * Math.cos(dlat2) * Math.cos(degreesLngBsAs-dlng2));

        return Math.round((Math.toDegrees(Math.acos(distance)) / 360) * earthCir);


    }



    /**
     * Carga los datos del cambio en base de datos si el document esta vacio
     * @param restTemplate
     * @throws JsonProcessingException
     */
    public void loadCurrencyOnDatabase(RestTemplate restTemplate) throws JsonProcessingException {

        if(rateRepository.findAll().isEmpty() || rateRepository.findAll() == null) {
            logger.info("esta cargando desde el servicio los datos actuales de cambio en la base");
            Map<String, Object> currencyVariables = new HashMap<>();
            currencyVariables.put("apikey", curencyApiKey);
            String currencyResponse = restTemplate.getForObject(curencyEndpoint, String.class, currencyVariables);
            logger.info(currencyResponse);
            Map<String, Object> result = new ObjectMapper().readValue(currencyResponse, HashMap.class);
            LinkedHashMap<Object, Object> rates = (LinkedHashMap<Object, Object>) result.get("rates");
            LocalDateTime localDateTime = LocalDateTime.now();
            rates.entrySet().stream().forEach(e -> rateRepository.save(new Rate(localDateTime, e.getKey().toString(), Double.parseDouble(e.getValue().toString()))));
        }

    }


    public void saveStats(TraceResponse traceResponse){

        if(statsRepository.findByCountry(traceResponse.getCountry())!=null) {
            Stats stats = statsRepository.findByCountry(traceResponse.getCountry());
            stats.setInvocations(stats.getInvocations() + 1);
            statsRepository.save(stats);

        }else{

            statsRepository.save(new Stats(traceResponse.getCountry(),traceResponse.getEstimated_distance(),1));

        }
    }


}
