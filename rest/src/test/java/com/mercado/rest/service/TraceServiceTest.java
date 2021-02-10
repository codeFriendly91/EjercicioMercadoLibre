package com.mercado.rest.service;

import com.google.gson.Gson;
import com.mercado.rest.document.Rate;
import com.mercado.rest.dto.countryservice.response.Country;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import com.mercado.rest.dto.traceService.TraceRequest;
import com.mercado.rest.dto.traceService.TraceResponse;
import com.mercado.rest.repository.RateRepository;
import com.mercado.rest.repository.StatsRepository;
import com.mercado.rest.repository.TraceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith({SpringExtension.class})
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TraceServiceTest {

    @Autowired
    TraceService traceService;

    @MockBean
    private RateRepository rateRepository;
    @MockBean
    private TraceRepository traceRepository;
    @MockBean
    private StatsRepository statsRepository;
    @MockBean
    private IpRestService ipRestService;
    @MockBean
    private CurrencyRestService currencyRestService;
    @MockBean
    private CountryRestService countryRestService;

    TraceResponse traceResponse;
    Country country;

    @BeforeEach
    public void setUp() throws Exception {

        String traceJson = "{\n" +
                "   \"ip\": \"45.5.15.0\",\n" +
                "   \"date\": \"09/02/2021 22:03:57\",\n" +
                "   \"country\": \"Argentina\",\n" +
                "   \"isoCode\": \"AR\",\n" +
                "   \"languages\":    [\n" +
                "      \"Spanish(spa)\",\n" +
                "      \"GuaranÃ\u00AD(grn)\"\n" +
                "   ],\n" +
                "   \"currency\": \"1 EUR = 106.989295 ARS\",\n" +
                "   \"times\": [\"22:03:57 (UTC-03)\"],\n" +
                "   \"estimated_distance(Km)\": 0\n" +
                "}";


        String countryJson = "{\n" +
                "   \"name\": \"Argentina\",\n" +
                "   \"topLevelDomain\": [\".ar\"],\n" +
                "   \"alpha2Code\": \"AR\",\n" +
                "   \"alpha3Code\": \"ARG\",\n" +
                "   \"callingCodes\": [\"54\"],\n" +
                "   \"capital\": \"Buenos Aires\",\n" +
                "   \"altSpellings\":    [\n" +
                "      \"AR\",\n" +
                "      \"Argentine Republic\",\n" +
                "      \"República Argentina\"\n" +
                "   ],\n" +
                "   \"region\": \"Americas\",\n" +
                "   \"subregion\": \"South America\",\n" +
                "   \"population\": 43590400,\n" +
                "   \"latlng\":    [\n" +
                "      -34,\n" +
                "      -64\n" +
                "   ],\n" +
                "   \"demonym\": \"Argentinean\",\n" +
                "   \"area\": 2780400,\n" +
                "   \"gini\": 44.5,\n" +
                "   \"timezones\": [\"UTC-03:00\"],\n" +
                "   \"borders\":    [\n" +
                "      \"BOL\",\n" +
                "      \"BRA\",\n" +
                "      \"CHL\",\n" +
                "      \"PRY\",\n" +
                "      \"URY\"\n" +
                "   ],\n" +
                "   \"nativeName\": \"Argentina\",\n" +
                "   \"numericCode\": \"032\",\n" +
                "   \"currencies\": [   {\n" +
                "      \"code\": \"ARS\",\n" +
                "      \"name\": \"Argentine peso\",\n" +
                "      \"symbol\": \"$\"\n" +
                "   }],\n" +
                "   \"languages\":    [\n" +
                "            {\n" +
                "         \"iso6391\": \"es\",\n" +
                "         \"iso6392\": \"spa\",\n" +
                "         \"name\": \"Spanish\",\n" +
                "         \"nativeName\": \"Español\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"iso6391\": \"gn\",\n" +
                "         \"iso6392\": \"grn\",\n" +
                "         \"name\": \"Guaraní\",\n" +
                "         \"nativeName\": \"Avañe'ẽ\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"translations\":    {\n" +
                "      \"de\": \"Argentinien\",\n" +
                "      \"es\": \"Argentina\",\n" +
                "      \"fr\": \"Argentine\",\n" +
                "      \"ja\": \"アルゼンチン\",\n" +
                "      \"it\": \"Argentina\",\n" +
                "      \"br\": \"Argentina\",\n" +
                "      \"pt\": \"Argentina\",\n" +
                "      \"nl\": \"Argentinië\",\n" +
                "      \"hr\": \"Argentina\",\n" +
                "      \"fa\": \"آرژانتین\"\n" +
                "   },\n" +
                "   \"flag\": \"https://restcountries.eu/data/arg.svg\",\n" +
                "   \"regionalBlocs\": [   {\n" +
                "      \"acronym\": \"USAN\",\n" +
                "      \"name\": \"Union of South American Nations\",\n" +
                "      \"otherAcronyms\":       [\n" +
                "         \"UNASUR\",\n" +
                "         \"UNASUL\",\n" +
                "         \"UZAN\"\n" +
                "      ],\n" +
                "      \"otherNames\":       [\n" +
                "         \"Unión de Naciones Suramericanas\",\n" +
                "         \"União de Nações Sul-Americanas\",\n" +
                "         \"Unie van Zuid-Amerikaanse Naties\",\n" +
                "         \"South American Union\"\n" +
                "      ]\n" +
                "   }],\n" +
                "   \"cioc\": \"ARG\"\n" +
                "}";

        traceResponse = new Gson().fromJson(traceJson, TraceResponse.class);
        country = new Gson().fromJson(countryJson,Country.class);
        List<String> timeZones = new ArrayList();
        timeZones.add("UTC-03:00");
        traceResponse.setTimeZones(timeZones);
        traceResponse.setCurrencyCode("ARS");
        when(countryRestService.get("ARG")).thenReturn(country);
        TraceRequest traceRequest = new TraceRequest("45.5.15.3");
        when(ipRestService.get(traceRequest.getIp())).thenReturn(new IpServiceResponse("AR","ARG","Argentina","AR"));
        doNothing().when(currencyRestService).get();
        when(traceRepository.findByIsoCode("AR")).thenReturn(traceResponse);
        when(rateRepository.findByCurrency("ARS")).thenReturn(new Rate(LocalDateTime.now(),"ARS",106.989295));

    }


    @Test
    void trace() throws Exception {

        when(traceRepository.existsByIsoCode("AR")).thenReturn(true);
        TraceRequest traceRequest = new TraceRequest("45.5.15.3");
        TraceResponse traceResponse = traceService.trace(traceRequest);
        assertEquals("Argentina",traceResponse.getCountry());
        assertEquals("AR",traceResponse.getIsoCode());
        assertEquals("1 EUR = 106.989295 ARS", traceResponse.getCurrency());
        assertEquals(0, traceResponse.getEstimatedDistance());
        verify(traceRepository).existsByIsoCode("AR");

    }

    @Test
    void traceGetFromService() throws Exception {


        when(traceRepository.existsByIsoCode("ARG")).thenReturn(true);
        TraceRequest traceRequest = new TraceRequest("45.5.15.3");
        TraceResponse traceResponse = traceService.trace(traceRequest);
        assertEquals("Argentina",traceResponse.getCountry());
        assertEquals("AR",traceResponse.getIsoCode());
        assertEquals("1 EUR = 106.989295 ARS", traceResponse.getCurrency());

    }



    @Test
    void getDistanceToBsAs() {

        //distancia hacia Bs As
        double distanceToBs = traceService.getDistanceToBsAs(-34,-64);
        assertEquals(0,distanceToBs);

        //distancia aprox españa
        double distanceToEsp = traceService.getDistanceToBsAs(40,-4);
        assertEquals(10267, distanceToEsp);

    }

}