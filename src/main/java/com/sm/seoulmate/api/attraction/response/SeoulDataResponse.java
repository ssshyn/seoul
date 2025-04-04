package com.sm.seoulmate.api.attraction.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sm.seoulmate.dto.TbVwAttractions;
import com.sm.seoulmate.dto.TvSeoulMetroStoreInfoResponse;
import com.sm.seoulmate.dto.ViewNightSpot;
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
