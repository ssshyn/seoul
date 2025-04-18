package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightViewFeignResponse {
    @JsonProperty("NUM")
    private Long originId;
    @JsonProperty("SUBJECT_CD")
    private String subjectCd;
    @JsonProperty("TITLE")
    private String name;
    @JsonProperty("ADDR")
    private String address;
    @JsonProperty("LA")
    private String locationY;
    @JsonProperty("LO")
    private String locationX;
    @JsonProperty("TEL_NO")
    private String tel;
    @JsonProperty("URL")
    private String homageUrl;
    @JsonProperty("OPERATING_TIME")
    private String operatingTime;
    @JsonProperty("FREE_YN")
    private String freeYn;
    @JsonProperty("ENTR_FREE")
    private String entrFree;
    @JsonProperty("CONTENTS")
    private String description;
    @JsonProperty("SUBWAY")
    private String subwayInfo;
}
