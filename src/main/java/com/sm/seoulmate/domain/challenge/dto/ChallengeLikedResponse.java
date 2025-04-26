package com.sm.seoulmate.domain.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChallengeLikedResponse(
        @Schema(description = "데이터 id")
        Long id,
        @Schema(description = "좋아요 여부")
        Boolean isLiked
){
}
