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
public class SeoulDataResponse {
    @JsonProperty("tvSeoulmetroStoreInfo")
    private TvSeoulMetroStoreInfoResponse tvSeoulMetroStoreInfo;
    @JsonProperty("TbVwAttractions")
    private TbVwAttractions tbVwAttractions;
    @JsonProperty("viewNightSpot")
    private ViewNightSpot viewNightSpot;
}
