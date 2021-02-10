package com.mercado.rest.dto.statsservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercado.rest.document.Stats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StatsServiceResponse {

    @JsonProperty ("averageDistance(Km)")
    private long averageDistance;
    @JsonProperty("min_max_distance(Km)")
    private List<Stats> statsList;

}
