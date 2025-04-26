package com.sm.seoulmate.domain.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NearbyAttractionDto {
    private Long attractionId;
    private Double locationX;  // 경도
    private Double locationY;  // 위도
    private Double distance;   // 계산된 거리
}
