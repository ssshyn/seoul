package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeoulDataResponse<T> {
    @Schema(description = "관광명소 response")
    @JsonProperty("TbVwAttractions")
    private SeoulApiResponse<TourSpotFeignResponse> tourSpotFeignResponse;

    @Schema(description = "야경명소 response")
    @JsonProperty("viewNightSpot")
    private SeoulApiResponse<NightViewFeignResponse> nightViewFeignResponse;

    @Schema(description = "산과공원 response")
    @JsonProperty("SebcParkTourKor")
    private SeoulApiResponse<MountainParkFeignResponse> mountainParkFeignResponse;

    @Schema(description = "공공한옥 response")
    @JsonProperty("ViewBcgImpFacil")
    private SeoulApiResponse<HanokFeignResponse> hanokFeignResponse;

    @Schema(description = "관광거리 response")
    @JsonProperty("SebcTourStreetKor")
    private SeoulApiResponse<TourRoadFeignResponse> tourRoadFeignResponse;
}
