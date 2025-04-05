package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeoulItem {
    @JsonProperty("SHPS_NO")
    private String shpsNo;
    @JsonProperty("STN_NM")
    private String stnNm;
    @JsonProperty("STN_CD")
    private String stnCd;
    @JsonProperty("TPBIZ")
    private String tpbiz;
    @JsonProperty("AREA")
    private String area;
    @JsonProperty("MM_RTFE")
    private String mmRtfe;
    @JsonProperty("CTRT_STRT_DD")
    private String ctrtStrtDd;
    @JsonProperty("CTRT_END_DD")
    private String ctrtEndDd;
}
