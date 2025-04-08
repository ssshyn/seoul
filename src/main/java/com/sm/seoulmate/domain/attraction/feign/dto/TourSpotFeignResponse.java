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
    private String postSn;
    @JsonProperty("LANG_CODE_ID")
    private String langCodeId;
    @JsonProperty("POST_SJ")
    private String postSj;
    @JsonProperty("POST_URL")
    private String postUrl;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("NEW_ADDRESS")
    private String newAddress;
    @JsonProperty("CMMN_TELNO")
    private String cmmnTelNo;
    @JsonProperty("CMMN_FAX")
    private String cmmnFax;
    @JsonProperty("CMMN_HMPG_URL")
    private String cmmnHmpgUrl;
    @JsonProperty("CMMN_USE_TIME")
    private String cmmnUseTime;
    @JsonProperty("CMMN_BSNDE")
    private String cmmnBsnde;
    @JsonProperty("CMMN_RSTDE")
    private String cmmnRstde;
    @JsonProperty("SUBWAY_INFO")
    private String subwayInfo;
    @JsonProperty("TAG")
    private String tag;
    @JsonProperty("BF_DESC")
    private String bfDesc;
}
