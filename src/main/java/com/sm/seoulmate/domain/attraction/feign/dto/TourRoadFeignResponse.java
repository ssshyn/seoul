package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourRoadFeignResponse {
    @JsonProperty("MAIN_KEY")
    private String originId;
    @JsonProperty("NAME_KOR")
    private String name;
    @JsonProperty("ADD_KOR")
    private String address;
    @JsonProperty("WGS84_X")
    private String locationX;
    @JsonProperty("WGS84_Y")
    private String locationY;
}