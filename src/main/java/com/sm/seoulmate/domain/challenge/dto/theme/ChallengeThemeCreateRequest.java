package com.sm.seoulmate.domain.challenge.dto.theme;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChallengeThemeCreateRequest(
        @Schema(description = "챌린지 테마명(국문)", example = "지역 탐방")
        String nameKor,
        @Schema(description = "챌린지 테마명(영문)", example = "Local Tour")
        String nameEng,
        @Schema(description = "챌린지 테마 설명(국문)", example = "특정 지역(자치구/동네)을 중심으로 구성된 투어")
        String descriptionKor,
        @Schema(description = "챌린지 테마 설명(영문)", example = "Tour centered around a specific area (district/neighborhood)")
        String descriptionEng
) {
}
