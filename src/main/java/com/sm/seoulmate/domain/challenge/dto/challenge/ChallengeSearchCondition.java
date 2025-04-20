package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChallengeSearchCondition(
        @Schema(description = "검색 키워드")
        String keyword
) {
}
