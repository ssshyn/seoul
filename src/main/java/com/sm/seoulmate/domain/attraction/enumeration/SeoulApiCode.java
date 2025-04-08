package com.sm.seoulmate.domain.attraction.enumeration;

import com.sm.seoulmate.domain.attraction.feign.dto.MountainParkFeignResponse;
import com.sm.seoulmate.domain.attraction.feign.dto.NightViewFeignResponse;
import com.sm.seoulmate.domain.attraction.feign.dto.TourSpotFeignResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum SeoulApiCode {
    TOUR_SPOT("TbVwAttractions", "관광명소 api", TourSpotFeignResponse.class),
    NIGHT_VIEW("viewNightSpot", "야경명소 api", NightViewFeignResponse.class),
    MOUNTAIN_PARK("SebcParkTourKor", "산과공원 api", MountainParkFeignResponse.class);

    final String apiCode;
    final String description;
    final Class<?> classType;
}
