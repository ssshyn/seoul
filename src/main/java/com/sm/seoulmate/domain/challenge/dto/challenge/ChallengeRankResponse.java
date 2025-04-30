package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeRankResponse {
        @Schema(description = "챌린지 id", example = "3")
        private Long id;
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
        private String name;
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
        private String title;
        @Schema(description = "챌린지 이미지 url")
        private String imageUrl;
        @Schema(description = "(로그인) 챌린지 찜 여부", example = "true")
        private Boolean isLiked;
        @Schema(description = "참여자 수", example = "12")
        private Integer progressCount;
}
