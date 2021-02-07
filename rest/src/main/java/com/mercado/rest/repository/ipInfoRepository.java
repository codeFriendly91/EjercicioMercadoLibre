package com.mercado.rest.repository;

import com.mercado.rest.document.IpInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ipInfoRepository extends MongoRepository <IpInfo, Integer> {

    IpInfo findByCurrency (String currency);

}
