package com.mercado.rest.repository;

import com.mercado.rest.document.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends MongoRepository <Stats, Integer> {

    Stats findByCountry (String country);
    Stats findTopByOrderByDistanceDesc();
    Stats findTopByOrderByDistanceAsc();


}
