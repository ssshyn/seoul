package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SearchResponse(
        @Schema(description = "관광지 정보")
        List<SearchAttraction> attractions,
        @Schema(description = "챌린지 정보")
        List<SearchChallenge> challenges
) {
}
