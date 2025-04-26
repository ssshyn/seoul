package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AttractionLikedResponse(
        @Schema(description = "관광지 id")
        Long id,
        @Schema(description = "좋아요 여부")
        Boolean isLiked
){
}
