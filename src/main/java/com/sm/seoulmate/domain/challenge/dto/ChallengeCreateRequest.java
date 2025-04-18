package com.sm.seoulmate.domain.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChallengeCreateRequest(
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
        String name,
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
        String title,
        @Schema(description = "챌린지 설명", example = "전통시장과 공공 문화시설이 어우러진 서촌 일대의 지역적 매력을 모두 담아보세요.")
        String description,
        @Size(min = 1, max = 5, message = "관광지 ID 는 필수값이며, 최대 5개까지 등록 가능합니다.")
        @Schema(description = "관광지 Id 목록", example = "[36, 21, 118, 356]")
        List<Long> attractionIdList,
        @Schema(description = "메인 관광지 Id", example = "163")
        Long mainAttractionId,
        @Schema(description = "핵심 장소", example = "청와대")
        String mainBorough,
        @Schema(description = "챌린지 난이도", example = "3")
        Integer level,
        @Schema(description = "챌린지 테마", example = "1")
        Long challengeThemeId
) { }
