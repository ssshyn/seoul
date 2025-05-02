package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.entity.CulturalEvent;
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
                .imageUrl(dto.imageUrl())
                .attractionIds(attractionIds)
                .mainLocation(dto.mainLocation())
                .mainLocationEng(dto.mainLocationEng())
                .challengeTheme(theme)
                .displayRank(dto.displayRank())
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
                .imageUrl(dto.imageUrl())
                .mainLocation(dto.mainLocation())
                .mainLocationEng(dto.mainLocationEng())
                .challengeTheme(theme)
                .displayRank(dto.displayRank())
                .build();
    }

    public static ChallengeResponse toProdResponse(Challenge entity) {
        return ChallengeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .displayRank(entity.getDisplayRank())
                .challengeThemeId(entity.getChallengeTheme().getId())
                .challengeThemeName(entity.getChallengeTheme().getNameKor())
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

    public static ChallengeResponse toResponse(Challenge entity, LanguageCode languageCode, Boolean isLiked) {
        boolean isKorean = Objects.equals(languageCode, LanguageCode.KOR);

        return ChallengeResponse.builder()
                .id(entity.getId())
                .name(isKorean ? entity.getName() : entity.getNameEng())
                .title(isKorean ? entity.getTitle() : entity.getTitleEng())
                .description(isKorean ? entity.getDescription() : entity.getDescriptionEng())
                .imageUrl(entity.getImageUrl())
                .likes(entity.getLikes().size())
                .isLiked(isLiked)
                .commentCount(entity.getComments().size())
                .attractionCount(entity.getAttractionIds().size())
                .mainLocation(isKorean ? entity.getMainLocation() : entity.getMainLocationEng())
                .challengeThemeId(entity.getChallengeTheme().getId())
                .challengeThemeName(isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng())
                .displayRank(entity.getDisplayRank())
                .level(entity.getLevel())
                .build();
    }

    public static ChallengeDetailResponse toDetailResponse(Challenge entity, LanguageCode languageCode, Boolean isLiked, Integer stampCount, ChallengeStatusCode challengeStatusCode) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);
        List<ChallenegeAttractionResponse> attractions = entity.getAttractionIds().stream().map(at -> AttractionMapper.toChallengeResponse(at, languageCode)).toList();

        List<CommentResponse> comments = entity.getComments().stream().map(comment -> CommentMapper.toResponse(comment, languageCode)).toList();
        return new ChallengeDetailResponse(
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
                isKorean ? entity.getMainLocation() : entity.getMainLocationEng(),
                entity.getChallengeTheme().getId(),
                isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng(),
                comments.stream().sorted(Comparator.comparing(CommentResponse::createdAt).reversed()).limit(10).toList(),
                stampCount,
                entity.getAttractionIds().size() == 1 ? entity.getAttractionIds().get(0).getAttractionInfos().get(0).getHomepageUrl() : null,
                entity.getAttractionIds().size() == 1 ? entity.getCulturalEvent().getStartDate() : null,
                entity.getAttractionIds().size() == 1 ? entity.getCulturalEvent().getEndDate() : null
        );
    }

    public static ChallengeRankResponse toRankResponse(Challenge entity, LanguageCode languageCode, Boolean isLiked) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);

        return new ChallengeRankResponse(
                entity.getId(),
                isKorean ? entity.getName() : entity.getNameEng(),
                isKorean ? entity.getTitle() : entity.getTitleEng(),
                entity.getImageUrl(),
                isLiked,
                entity.getStatuses().size()
        );
    }

    public static CulturalChallenge toCulturalChallenge(CulturalEvent culturalEvent, LanguageCode languageCode, Boolean isLiked, String homepageUrl) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);
        String title = isKorean ? culturalEvent.getCultureTheme().getPrefixTitle(culturalEvent.getCulturePeriod(), culturalEvent.getChallenge().getName())
                : culturalEvent.getChallenge().getNameEng();

        return CulturalChallenge.builder()
                .id(culturalEvent.getChallenge().getId())
                .title(title)
                .startDate(culturalEvent.getStartDate())
                .endDate(culturalEvent.getEndDate())
                .imageUrl(culturalEvent.getChallenge().getImageUrl())
                .hompageUrl(homepageUrl)
                .isLiked(isLiked)
                .build();
    }
}
