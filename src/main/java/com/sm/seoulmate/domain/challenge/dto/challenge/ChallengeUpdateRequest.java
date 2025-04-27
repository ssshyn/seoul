package com.sm.seoulmate.domain.challenge.dto.challenge;

import com.sm.seoulmate.domain.challenge.enumeration.DisplayRank;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record ChallengeUpdateRequest(
        @Schema(description = "챌린지 id", example = "3")
        Long id,
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루", requiredMode = REQUIRED)
        String name,
        @Schema(description = "챌린지 이름(영문)", example = "A Day in Seochon's Alleyways", requiredMode = REQUIRED)
        String nameEng,
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지", requiredMode = REQUIRED)
        String title,
        @Schema(description = "챌린지 타이틀(영문)", example = "From Tongin Market to the Blue House Front Road", requiredMode = REQUIRED)
        String titleEng,
        @Schema(description = "챌린지 설명", example = "전통시장과 공공 문화시설이 어우러진 서촌 일대의 지역적 매력을 모두 담아보세요.")
        String description,
        @Schema(description = "챌린지 설명(영문)", example = "Experience the unique charm of Seochon, where traditional markets blend seamlessly with public cultural spaces.")
        String descriptionEng,
        @Schema(description = "관광지 Id 목록", example = "[36, 21, 118, 356]")
        List<Long> attractionIdList,
        @Schema(description = "주요 동네", example = "서촌")
        String mainLocation,
        @Schema(description = "노출 우선 순위", example = "MEDIUM")
        DisplayRank displayRank,
        @Schema(description = "챌린지 테마", example = "1")
        Long challengeThemeId
) {
}
