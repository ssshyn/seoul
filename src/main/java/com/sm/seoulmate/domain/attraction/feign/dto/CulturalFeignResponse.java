package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalFeignResponse {
    @JsonProperty("CODENAME")
    private String codeName;
    @JsonProperty("GUNAME")
    private String gunName;
    @JsonProperty("TITLE")
    private String title;
    @JsonProperty("ETC_DESC")
    private String description;
    @JsonProperty("STRTDATE")
    private String startDate;
    @JsonProperty("END_DATE")
    private String endDate;
    @JsonProperty("PLACE")
    private String address;
    @JsonProperty("LAT")
    private String locationX;
    @JsonProperty("LOT")
    private String locationY;
    @JsonProperty("MAIN_IMG")
    private String imageUrl;
    @JsonProperty("ORG_LINK")
    private String homepage;
}
