package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;

public class ChallengeThemeMapper {
    public static ChallengeTheme toEntity(ChallengeThemeCreateRequest dto) {
        return ChallengeTheme.builder()
                .nameKor(dto.nameKor())
                .nameEng(dto.nameEng())
                .descriptionKor(dto.descriptionKor())
                .descriptionEng(dto.descriptionEng())
                .build();
    }
    public static ChallengeThemeResponse toResponse(ChallengeTheme entity) {
        return new ChallengeThemeResponse(
                entity.getId(),
                entity.getNameKor(),
                entity.getNameEng(),
                entity.getDescriptionKor(),
                entity.getDescriptionEng()
        );
    }
}
