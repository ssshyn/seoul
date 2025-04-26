package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeResponse {
    @Schema(description = "챌린지 id", example = "3")
    private Long id;
    @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
    private String name;
    @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
    private String title;
    @Schema(description = "챌린지 설명", example = "전통시장과 공공 문화시설이 어우러진 서촌 일대의 지역적 매력을 모두 담아보세요.")
    private String description;
    @Schema(description = "챌린지 이미지 url")
    private String imageUrl;
    @Schema(description = "좋아요 수", example = "10")
    private Integer likes;
    @Schema(description = "(로그인) 챌린지 찜 여부", example = "true")
    private Boolean isLiked;
    @Schema(description = "댓글 수", example = "2")
    private Integer commentCount;
    @Schema(description = "관광지 수", example = "5")
    private Integer attractionCount;
    @Schema(description = "내 스탬프 수", example = "2")
    private Integer myStampCount;
    @Schema(description = "주요 동네", example = "서촌")
    private String mainLocation;
    @Schema(description = "챌린지 테마 id", example = "1")
    private Long challengeThemeId;
    @Schema(description = "챌린지 테마명", example = "지역 탐방")
    private String challengeThemeName;
    @Schema(description = "나로부터 거리")
    private Long distance;
}
