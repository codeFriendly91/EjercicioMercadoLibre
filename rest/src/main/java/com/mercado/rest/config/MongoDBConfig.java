package com.mercado.rest.config;


import com.mercado.rest.document.Rate;
import com.mercado.rest.repository.RateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@EnableMongoRepositories(basePackageClasses = RateRepository.class)
@Configuration
public class MongoDBConfig {


    @Bean
    CommandLineRunner commandLineRunner (RateRepository rateRepository){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return string ->{

        };

    }

}
