package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AttractionSearchCondition(
        @Schema(description = "검색 키워드")
        String keyword
) {
}
