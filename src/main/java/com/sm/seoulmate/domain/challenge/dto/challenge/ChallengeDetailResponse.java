package com.sm.seoulmate.domain.challenge.dto.challenge;

import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ChallengeDetailResponse(
        @Schema(description = "챌린지 id", example = "3")
        Long id,
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
        String name,
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
        String title,
        @Schema(description = "챌린지 설명", example = "전통시장과 공공 문화시설이 어우러진 서촌 일대의 지역적 매력을 모두 담아보세요.")
        String description,
        @Schema(description = "챌린지 이미지 url")
        String imageUrl,
        @Schema(description = "챌린지 찜 수")
        Integer likedCount,
        @Schema(description = "챌린지 진행 사람 수")
        Integer progressCount,
        @Schema(description = "챌린지 장소 수")
        Integer attractionCount,
        @Schema(description = "댓글 수")
        Integer commentCount,
        @Schema(description = "(로그인) 챌린지 찜 여부", example = "true")
        Boolean isLiked,
        @Schema(description = "(로그인) 챌린지 진행 상태", example = "PROGRESS")
        ChallengeStatusCode challengeStatusCode,
        @Schema(description = "관광지 목록")
        List<ChallenegeAttractionResponse> attractions,
        @Schema(description = "주요 동네")
        String mainLocation,
        @Schema(description = "챌린지 테마 id", example = "1")
        Long challengeThemeId,
        @Schema(description = "챌린지 테마명", example = "지역 탐방")
        String challengeThemeName,
        @Schema(description = "챌린지 댓글 목록")
        List<CommentResponse> comments,
        @Schema(description = "내가 찍은 스탬프 수")
        Integer myStampCount,
        @Schema(description = "홈페이지 url")
        String homepageUrl,
        @Schema(description = "운영시간(시작)")
        LocalDate startDate,
        @Schema(description = "운영시간(종료)")
        LocalDate endDate
) {
}
