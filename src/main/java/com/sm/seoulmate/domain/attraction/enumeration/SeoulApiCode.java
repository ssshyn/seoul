package com.sm.seoulmate.domain.attraction.enumeration;

import com.sm.seoulmate.domain.attraction.feign.dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeoulApiCode {
    TOUR_SPOT("TbVwAttractions", "관광명소 api", TourSpotFeignResponse.class),
    NIGHT_VIEW("viewNightSpot", "야경명소 api", NightViewFeignResponse.class),
    MOUNTAIN_PARK("SebcParkTourKor", "산과공원 api", MountainParkFeignResponse.class),
    HANOK("ViewBcgImpFacil", "공공한옥 api", HanokFeignResponse .class),
    TOUR_ROAD("SebcTourStreetKor", "관광거리 api", TourRoadFeignResponse.class),
    CULTURAL("culturalEventInfo", "문화행사 api", CulturalFeignResponse.class);

    final String apiCode;
    final String description;
    final Class<?> classType;
}
