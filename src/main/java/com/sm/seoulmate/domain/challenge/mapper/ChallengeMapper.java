package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.challenge.dto.ChallengeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.ChallengeResponse;
import com.sm.seoulmate.domain.challenge.dto.ChallengeUpdateRequest;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;

import java.util.List;

public class ChallengeMapper {
    public static Challenge toEntity(ChallengeCreateRequest dto, ChallengeTheme theme, List<AttractionId> attractionIds) {
        return Challenge.builder()
                .name(dto.name())
                .title(dto.title())
                .description(dto.description())
                .attractionIds(attractionIds)
                .mainAttractionId(dto.mainAttractionId())
                .mainBorough(dto.mainBorough())
                .level(dto.level())
                .challengeTheme(theme)
                .build();
    }

    public static Challenge toUpdatedEntity(ChallengeUpdateRequest dto, ChallengeTheme theme, List<AttractionId> attractionIds) {
        return Challenge.builder()
                .id(dto.id()) // 수정 시 ID는 그대로 유지해야 함
                .name(dto.name())
                .title(dto.title())
                .description(dto.description())
                .attractionIds(attractionIds)
                .mainAttractionId(dto.mainAttractionId())
                .mainBorough(dto.mainBorough())
                .level(dto.level())
                .challengeTheme(theme)
                .build();
    }

    public static ChallengeResponse toResponse(Challenge entity) {
        List<Long> attractionIdList = entity.getAttractionIds().stream().map(AttractionId::getId).toList();

        return new ChallengeResponse(
                entity.getId(),
                entity.getName(),
                entity.getTitle(),
                entity.getDescription(),
                attractionIdList,
                entity.getMainAttractionId(),
                entity.getMainBorough(),
                entity.getLevel(),
                entity.getChallengeTheme().getId(),
                entity.getComments()
        );
    }
}
