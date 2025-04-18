package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotFeignResponse {
    @JsonProperty("POST_SN")
    private String originId;
    @JsonProperty("LANG_CODE_ID")
    private String languageCode;
    @JsonProperty("POST_SJ")
    private String name;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("NEW_ADDRESS")
    private String newAddress;
    @JsonProperty("CMMN_TELNO")
    private String tel;
    @JsonProperty("CMMN_HMPG_URL")
    private String hompage;
    @JsonProperty("CMMN_USE_TIME")
    private String cmmnUseTime;
    @JsonProperty("CMMN_BSNDE")
    private String cmmnBsnde;
    @JsonProperty("CMMN_RSTDE")
    private String cmmnRstde;
    @JsonProperty("SUBWAY_INFO")
    private String subway;
    @JsonProperty("TAG")
    private String tag;
    @JsonProperty("BF_DESC")
    private String description;
}
