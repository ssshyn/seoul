package com.sm.seoulmate.domain.challenge.dto;

import com.sm.seoulmate.domain.challenge.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChallengeResponse(
        @Schema(description = "챌린지 id", example = "3")
        Long id,
        @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
        String name,
        @Schema(description = "챌린지 이름(영문)", example = "A Day in Seochon's Alleyways")
        String nameEng,
        @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
        String title,
        @Schema(description = "챌린지 타이틀(영문)", example = "From Tongin Market to the Blue House Front Road")
        String titleEng,
        @Schema(description = "챌린지 설명", example = "전통시장과 공공 문화시설이 어우러진 서촌 일대의 지역적 매력을 모두 담아보세요.")
        String description,
        @Schema(description = "챌린지 설명(영문)", example = "Experience the unique charm of Seochon, where traditional markets blend seamlessly with public cultural spaces.")
        String descriptionEng,
        @Schema(description = "관광지 Id 목록", example = "[36, 21, 118, 356]")
        List<Long> attractionIdList,
        @Schema(description = "메인 관광지 Id", example = "163")
        Long mainAttractionId,
        @Schema(description = "챌린지 난이도", example = "3")
        Integer level,
        @Schema(description = "챌린지 테마 id", example = "1")
        Long challengeThemeId,
        @Schema(description = "챌린지 댓글 목록")
        List<Comment> comments
) {}
