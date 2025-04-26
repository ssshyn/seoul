package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
                .challengeTheme(theme)
                .build();
    }

    public static ChallengeResponse toProdResponse(Challenge entity) {
        return ChallengeResponse.builder()
                .id(entity.getId())
                .build();
    }

    public static ChallengeResponse toResponseMy(Challenge entity, LanguageCode languageCode, User user) {
        boolean isKorean = Objects.equals(languageCode, LanguageCode.KOR);
        return ChallengeResponse.builder()
                .id(entity.getId())
                .name(isKorean ? entity.getName() : entity.getNameEng())
                .title(isKorean ? entity.getTitle() : entity.getTitleEng())
                .likes(entity.getLikes().size())
                .commentCount(entity.getComments().size())
                .attractionCount(entity.getAttractionIds().size())
                .challengeThemeId(entity.getChallengeTheme().getId())
                .challengeThemeName(isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng())
                .build();
    }

    public static ChallengeResponsess toResponsess(Challenge entity) {
        List<Long> attractionIdList = entity.getAttractionIds().stream().map(AttractionId::getId).toList();

        return new ChallengeResponsess(
                entity.getId(),
                entity.getName(),
                entity.getNameEng(),
                entity.getTitle(),
                entity.getTitleEng(),
                entity.getDescription(),
                entity.getDescriptionEng(),
                entity.getImageUrl(),
                attractionIdList,
                entity.getMainLocation(),
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
                entity.getImageUrl(),
                entity.getLikes().size(),
                entity.getStatuses().size(),
                attractions.size(),
                entity.getComments().size(),
                isLiked,
                challengeStatusCode,
                attractions,
                entity.getMainLocation(),
                entity.getChallengeTheme().getId(),
                isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng(),
                entity.getComments().stream().sorted(Comparator.comparing(Comment::getCreatedAt).reversed()).limit(10).toList()
        );
    }
}
