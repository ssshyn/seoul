package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    @Schema(description = "x 좌표", example = "126.9798625")
    private Double locationX;
    @Schema(description = "y 좌표", example = "37.5772571")
    private Double locationY;
    @Schema(description = "거리 기준(단위:미터)", example = "150")
    private Integer radius;
    @Schema(description = "조회 개수", example = "10")
    private Integer limit;
}
