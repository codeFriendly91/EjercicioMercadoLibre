package com.mercado.rest.service;

import com.mercado.rest.Exception.ExceptionResponse;
import com.mercado.rest.Exception.RestException;
import com.mercado.rest.document.Stats;
import com.mercado.rest.dto.statsservice.StatsServiceResponse;
import com.mercado.rest.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final StatsRepository statsRepository;


    public StatsServiceResponse getMaxAndMinStats () throws RestException {

        StatsServiceResponse statsServiceResponse = new StatsServiceResponse();

        statsServiceResponse.setStatsList(new ArrayList<>());
        if (statsRepository.findAll() != null && !statsRepository.findAll().isEmpty()) {

            Stats maxStats = statsRepository.findTopByOrderByDistanceAsc();
            Stats minStats = statsRepository.findTopByOrderByDistanceDesc();

            if (minStats != null) {
                statsServiceResponse.getStatsList().add(maxStats);
            }

            if (maxStats != null && !minStats.equals(maxStats)) {
                statsServiceResponse.getStatsList().add(minStats);
            }

            statsServiceResponse.setAverageDistance(getAverageDistance(statsRepository.findAll()));

        }else{

            throw new RestException(200,"NO STATS FOUNDS");
        }
        return statsServiceResponse;


    }


    /**
     * Obtiene la distancia promedio de todas las ejecuciones
     * @param statsList
     * @return
     */
    public long getAverageDistance(List<Stats> statsList){
        long invocations = 0;
        long distance = 0;

        for(Stats stat : statsList){

            distance = distance + (stat.getDistance() * stat.getInvocations());
            invocations = invocations + stat.getInvocations();

        }
        return distance/invocations;
    }

}
