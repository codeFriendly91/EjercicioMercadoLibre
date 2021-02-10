package com.mercado.rest.service;


import com.mercado.rest.Exception.RestException;
import com.mercado.rest.dto.ipservice.response.IpServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IpRestService {


    @Value("${ip.country.endpoint}")
    private String ipEndPoint;


    public IpServiceResponse get (String ip) throws RestException {

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder sb = new StringBuilder();
        sb.append(ipEndPoint);
        sb.append(ip);


        ResponseEntity<IpServiceResponse> ipServiceResponseEntity = restTemplate.exchange(sb.toString(), HttpMethod.GET,null, IpServiceResponse.class);
        log.info(ipServiceResponseEntity.toString());
        System.out.println(ipServiceResponseEntity.getStatusCodeValue());
        log.info(String.valueOf(ipServiceResponseEntity.getStatusCodeValue()));
        if(ipServiceResponseEntity.getStatusCode().value() != 200){

            throw new RestException(ipServiceResponseEntity.getStatusCode().value(),"");

        }
        return ipServiceResponseEntity.getBody();
    }


}
