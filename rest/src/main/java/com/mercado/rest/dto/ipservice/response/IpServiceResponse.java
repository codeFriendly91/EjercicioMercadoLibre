package com.mercado.rest.dto.ipservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpServiceResponse {

    public String countryCode;
    public String countryCode3;
    public String countryName;
    public String countryEmoji;

}
