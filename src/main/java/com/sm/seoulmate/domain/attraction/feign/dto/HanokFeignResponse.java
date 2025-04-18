package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HanokFeignResponse {
    @JsonProperty("BIF_SEQ")
    private Long originId;
    @JsonProperty("BIF_TITLE")
    private String name;
    @JsonProperty("BIF_ADDR")
    private String address;
    @JsonProperty("BIF_GIS_X")
    private String locationX;
    @JsonProperty("BIF_GIS_Y")
    private String locationY;
    @JsonProperty("BIF_TEL")
    private String tel;
    @JsonProperty("BIF_HOMPAGE")
    private String homepage;
    @JsonProperty("BIF_OPENTIME")
    private String openTime;
    @JsonProperty("BIF_CLOSEDAY")
    private String closeDay;
    @JsonProperty("BIF_WAYINFO")
    private String subway;
    @JsonProperty("BIF_DESC")
    private String description;
}