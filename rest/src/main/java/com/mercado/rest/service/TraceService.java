package com.mercado.rest.service;

import com.mercado.rest.Exception.RestException;
import com.mercado.rest.document.Rate;
import com.mercado.rest.document.Stats;
import com.mercado.rest.dto.countryservice.response.Country;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import com.mercado.rest.dto.traceService.TraceRequest;
import com.mercado.rest.dto.traceService.TraceResponse;
import com.mercado.rest.repository.RateRepository;
import com.mercado.rest.repository.StatsRepository;
import com.mercado.rest.repository.TraceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraceService {


    private final  RateRepository rateRepository;
    private final  TraceRepository traceRepository;
    private final  StatsRepository statsRepository;
    private final  IpRestService ipRestService;
    private final  CurrencyRestService currencyRestService;
    private final  CountryRestService countryRestService;



    public TraceResponse trace(TraceRequest traceRequest) throws RestException {


        TraceResponse traceResponse;
        Country country;
        Rate rate;

        try {

            //execute ip API
            IpServiceResponse ipServiceResponse =ipRestService.get(traceRequest.getIp());

            //ejecuta servicio con datos del cambio y actualiza la base de datos en caso de que el repositorio este vacio
            currencyRestService.get();

            //Busco en la base si ya no tengo una respuesta anterior con la data
            //Si no devuelve nada ejecuto el servicio
            if(traceRepository.existsByIsoCode(ipServiceResponse.getCountryCode())) {

                traceResponse = traceRepository.findByIsoCode(ipServiceResponse.getCountryCode());
                mapResponseFromRepository(traceResponse,traceRequest);

            }else{

                country =  countryRestService.get(ipServiceResponse.getCountryCode3());
                rate = rateRepository.findByCurrency(country.getCurrencies().get(0).getCode());

                //Si no estan los datos del cambio en la base de datos los busco del servicio
                traceResponse = mapResponseFromService(traceRequest.getIp(), country, rate);

            }


        }catch (RestException restException ) {

            throw restException;
        }
        traceRepository.save(traceResponse);
        saveStats(traceResponse);
        return traceResponse;

    }


    /**
     * Mapea la respuesta final con los datos provenientes del servicio que devuelve los datos del un pais
     *
      * @param ip
     * @param country
     * @param rate
     * @return
     */
    public TraceResponse mapResponseFromService(String ip, Country country, Rate rate){

        TraceResponse traceResponse = new TraceResponse();
        traceResponse.setTimes(new ArrayList<>());
        traceResponse.setLanguages(new ArrayList<>());
        traceResponse.setTimeZones(new ArrayList<>());

        traceResponse.setIp(ip);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        traceResponse.setDate(dtf.format(now));
        traceResponse.setCountry(country.getNativeName());
        traceResponse.setIsoCode(country.getAlpha2Code());
        traceResponse.setEstimatedDistance(getDistanceToBsAs(country.getLatlng().get(0), country.getLatlng().get(1)));
        traceResponse.setCurrencyCode(country.getCurrencies().get(0).getCode());
        setCurrencyToTraceResponse(traceResponse,rate);


        country.getLanguages().stream().forEach((language) -> traceResponse.getLanguages().add(language.getName() + "(" + language.getIso6392() + ")"));
        country.getTimezones().stream().forEach((timeZone) -> traceResponse.getTimes().add(getCountryTime(timeZone)));
        country.getTimezones().stream().forEach((timeZone) -> traceResponse.getTimeZones().add(timeZone));

        return traceResponse;
    }



    /**
     * Mapea la respuesta final del servicio con los datos levantados del repo
     * @param traceResponse
     * @param traceRequest
     */

    public void mapResponseFromRepository(TraceResponse traceResponse,TraceRequest traceRequest) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        List<String> currentsTimes = new ArrayList<>();
        traceResponse.setDate(dtf.format(now));
        traceResponse.setIp(traceRequest.getIp());
        traceResponse.getTimeZones().stream().forEach((timeZone) -> currentsTimes.add(getCountryTime(timeZone)));
        traceResponse.setTimes(currentsTimes);
        Rate rate = rateRepository.findByCurrency(traceResponse.getCurrencyCode());
        setCurrencyToTraceResponse(traceResponse,rate);
        traceResponse.setCurrency("1 EUR = " +rate.getValue()+" " + traceResponse.getCurrencyCode());

    }




    /**
     * Obtiene la hora actual en formato string de un pais a partir del time-zone pasado como parametro ej:(UTC-3)
     * @param timeZone
     * @return
     */
    public String getCountryTime(String timeZone){


        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            String splitTimeZone = timeZone.split(":")[0];

            if (timeZone.contains("-")) {

                int hourOnly = Integer.parseInt(splitTimeZone.replaceAll("[^0-9]", ""));
                Instant instant = Instant.now().minus(hourOnly, ChronoUnit.HOURS);
                date = Date.from(instant);

                return formatter.format(date) + " (" + splitTimeZone + ")";

            } else if (timeZone.contains("+")) {

                int hourOnly = Integer.parseInt(splitTimeZone.replaceAll("[^0-9]", ""));
                Instant instant = Instant.now().plus(hourOnly, ChronoUnit.HOURS);
                date = Date.from(instant);

                return formatter.format(date) + " (" + splitTimeZone + ")";

            } else {

                Instant instant = Instant.now();
                date = Date.from(instant);
                return formatter.format(date) + " (" + splitTimeZone + ")";
            }
        }catch (Exception e){
            throw e;
        }
    }



    /**
     * Calcula la distancia desde Buenos Aires hasta la lat y lng pasadas como parametros
     * @param lat
     * @param lng
     * @return
     */

    public long getDistanceToBsAs(int lat, int lng){

        double distance;
        double earthCir = 40000;
        try {

            double degreesLatBsAs = Math.toRadians(-34);
            double degreesLngBsAs = Math.toRadians(-64);
            //circunferencia de la tierra
            double dlat2 = Math.toRadians(lat);
            double dlng2 = Math.toRadians(lng);
            //obtengo el angulo entre los dos puntos y regla de 3
            //regla de 3. si para circunferencia 360 = 40000
            distance = (Math.sin(degreesLatBsAs) * Math.sin(dlat2)) + (Math.cos(degreesLatBsAs) * Math.cos(dlat2) * Math.cos(degreesLngBsAs - dlng2));

        }catch (Exception e){

            throw e;

        }
        return Math.round((Math.toDegrees(Math.acos(distance)) / 360) * earthCir);

    }


    /**
     * Actualiza las estadisticas en la base de datos
     * @param traceResponse
     */
    public void saveStats(TraceResponse traceResponse){
        if(statsRepository.findByCountry(traceResponse.getCountry())!=null) {
            Stats stats = statsRepository.findByCountry(traceResponse.getCountry());
            stats.setInvocations(stats.getInvocations() + 1);
            statsRepository.save(stats);
        }else{

            statsRepository.save(new Stats(traceResponse.getCountry(),traceResponse.getEstimatedDistance(),1));

        }
    }

    public void setCurrencyToTraceResponse(TraceResponse traceResponse, Rate rate){

        traceResponse.setCurrency("1 EUR = " +rate.getValue()+" " + traceResponse.getCurrencyCode());;
    }

}
