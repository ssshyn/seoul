package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChallenegeAttractionResponse(
        @Schema(description = "관광지 id")
        Long id,
        @Schema(description = "관광지명")
        String name,
        @Schema(description = "X 좌표")
        String locationX,
        @Schema(description = "Y 좌표")
        String locationY,
        @Schema(description = "주소")
        String address,
        @Schema(description = "좋아요 여부")
        Boolean isLiked,
        @Schema(description = "좋아요 수")
        Integer likes,
        @Schema(description = "스탬프 여부")
        Boolean isStamped,
        @Schema(description = "스탬프 찍은 사람 수")
        Integer stampCount,
        @Schema(description = "이미지 url")
        String imageUrl
) {
}
