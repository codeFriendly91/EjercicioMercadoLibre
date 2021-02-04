package com.mercado.rest.dto.ipservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpServiceResponse {

    private String countryCode;
    private String countryCode3;
    private String countryName;
    private String countryEmoji;

}
