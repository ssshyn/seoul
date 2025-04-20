package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.challenge.dto.challenge.ChallenegeDetailResponse;
import com.sm.seoulmate.domain.challenge.dto.challenge.ChallengeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.challenge.ChallengeResponse;
import com.sm.seoulmate.domain.challenge.dto.challenge.ChallengeUpdateRequest;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;

import java.util.Comparator;
import java.util.List;

public class ChallengeMapper {
    public static Challenge toEntity(ChallengeCreateRequest dto, ChallengeTheme theme, List<AttractionId> attractionIds) {
        return Challenge.builder()
                .name(dto.name())
                .nameEng(dto.nameEng())
                .title(dto.title())
                .titleEng(dto.titleEng())
                .description(dto.description())
                .descriptionEng(dto.descriptionEng())
                .attractionIds(attractionIds)
                .mainAttractionId(dto.mainAttractionId())
                .level(dto.level())
                .challengeTheme(theme)
                .build();
    }

    public static Challenge toUpdatedEntity(ChallengeUpdateRequest dto, ChallengeTheme theme, List<AttractionId> attractionIds) {
        return Challenge.builder()
                .id(dto.id()) // 수정 시 ID는 그대로 유지해야 함
                .name(dto.name())
                .nameEng(dto.nameEng())
                .title(dto.title())
                .titleEng(dto.titleEng())
                .description(dto.description())
                .descriptionEng(dto.descriptionEng())
                .attractionIds(attractionIds)
                .mainAttractionId(dto.mainAttractionId())
                .level(dto.level())
                .challengeTheme(theme)
                .build();
    }

    public static ChallengeResponse toResponse(Challenge entity) {
        List<Long> attractionIdList = entity.getAttractionIds().stream().map(AttractionId::getId).toList();

        return new ChallengeResponse(
                entity.getId(),
                entity.getName(),
                entity.getNameEng(),
                entity.getTitle(),
                entity.getTitleEng(),
                entity.getDescription(),
                entity.getDescriptionEng(),
                attractionIdList,
                entity.getMainAttractionId(),
                entity.getLevel(),
                entity.getChallengeTheme().getId(),
                entity.getComments()
        );
    }

    public static ChallenegeDetailResponse toDetailResponse(Challenge entity, LanguageCode languageCode, Boolean isLiked, ChallengeStatusCode challengeStatusCode) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);
        List<ChallenegeAttractionResponse> attractions = entity.getAttractionIds().stream().map(at -> AttractionMapper.toChallengeResponse(at, languageCode)).toList();

        return new ChallenegeDetailResponse(
               entity.getId(),
               isKorean ? entity.getName() : entity.getNameEng(),
                isKorean ? entity.getTitle() : entity.getTitleEng(),
                isKorean ? entity.getDescription() : entity.getDescriptionEng(),
                entity.getLikes().size(),
                entity.getStatuses().size(),
                attractions.size(),
                entity.getComments().size(),
                isLiked,
                challengeStatusCode,
                attractions,
                entity.getMainAttractionId(),
                entity.getLevel(),
                entity.getChallengeTheme().getId(),
                isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng(),
                entity.getComments().stream().sorted(Comparator.comparing(Comment::getCreatedAt).reversed()).limit(10).toList()
        );
    }
}
