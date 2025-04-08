package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightViewFeignResponse {
    @JsonProperty("NUM")
    private Long num;
    @JsonProperty("SUBJECT_CD")
    private String subjectCd;
    @JsonProperty("TITLE")
    private String title;
    @JsonProperty("ADDR")
    private String address;
    @JsonProperty("LA")
    private String locationY;
    @JsonProperty("LO")
    private String locationX;
    @JsonProperty("TEL_NO")
    private String telNo;
    @JsonProperty("URL")
    private String homageUrl;
    @JsonProperty("OPERATING_TIME")
    private String operatingTime;
    @JsonProperty("FREE_YN")
    private String freeYn;
    @JsonProperty("ENTR_FREE")
    private String entrFree;
    @JsonProperty("CONTENTS")
    private String contents;
    @JsonProperty("SUBWAY")
    private String subwayInfo;
    @JsonProperty("BUS")
    private String busInfo;
    @JsonProperty("PARKING_INFO")
    private String parkingInfo;
    @JsonProperty("REG_DATE")
    private String regDate;
    @JsonProperty("MOD_DATE")
    private String modDate;
}
