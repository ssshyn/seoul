package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChallengeRankResponse (
        @Schema(description = "챌린지 id", example = "3")
        Long id,
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
        String name,
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
        String title,
        @Schema(description = "챌린지 이미지 url")
        String imageUrl,
        @Schema(description = "(로그인) 챌린지 찜 여부", example = "true")
        Boolean isLiked,
        @Schema(description = "참여자 수", example = "12")
        Integer progressCount
){
}
