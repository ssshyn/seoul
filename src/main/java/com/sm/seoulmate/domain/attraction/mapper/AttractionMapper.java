package com.sm.seoulmate.domain.attraction.mapper;

import com.sm.seoulmate.domain.attraction.dto.AttractionDetailResponse;
import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.util.UserInfoUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class AttractionMapper {
    public static AttractionDetailResponse toResponse(AttractionId entity, LanguageCode languageCode, Boolean isLiked) {
        AttractionInfo info = entity.getAttractionInfos().stream()
                .filter(attractionInfo -> Objects.equals(attractionInfo.getLanguageCode(), languageCode))
                .findFirst().orElse(new AttractionInfo());

        return new AttractionDetailResponse(
                entity.getId(),
                info.getName(),
                info.getDescription(),
                entity.getAttractionDetailCodes(),
                info.getAddress(),
                StringUtils.trimToNull(StringUtils.trimToEmpty(info.getOperDay()) + StringUtils.trimToEmpty(info.getOperOpenTime()) + StringUtils.trimToEmpty(info.getOperCloseTime())),
                info.getHomepageUrl(),
                info.getLocationX(),
                info.getLocationY(),
                info.getTel(),
                info.getSubway(),
                info.getImageUrl(),
                entity.getLikes().size(),
                isLiked
        );
    }

    public static ChallenegeAttractionResponse toChallengeResponse(AttractionId entity, LanguageCode languageCode) {
        AttractionInfo info = entity.getAttractionInfos().stream().filter(attractionInfo -> attractionInfo.getLanguageCode().equals(languageCode)).findFirst().orElse(new AttractionInfo());

        return new ChallenegeAttractionResponse(
                entity.getId(),
                info.getName(),
                info.getLocationX(),
                info.getLocationY(),
                entity.getLikes().stream().anyMatch(like -> Objects.equals(like.getUser().getId(), UserInfoUtil.getUserId())),
                entity.getLikes().size(),
                entity.getVisitStamps().stream().anyMatch(stamp -> Objects.equals(stamp.getUser().getId(), UserInfoUtil.getUserId())),
                entity.getVisitStamps().size()
        );
    }

    public static AttractionDetailResponse toLikesResponse(AttractionLikes entity, LanguageCode languageCode) {
        AttractionId id = entity.getAttraction();
        AttractionInfo info = id.getAttractionInfos().stream().filter(attractionInfo -> attractionInfo.getLanguageCode().equals(languageCode)).findFirst().orElse(new AttractionInfo());

        return new AttractionDetailResponse(
                id.getId(),
                info.getName(),
                info.getDescription(),
                id.getAttractionDetailCodes(),
                info.getAddress(),
                StringUtils.trimToNull(StringUtils.trimToEmpty(info.getOperDay()) + StringUtils.trimToEmpty(info.getOperOpenTime()) + StringUtils.trimToEmpty(info.getOperCloseTime())),
                info.getHomepageUrl(),
                info.getLocationX(),
                info.getLocationY(),
                info.getTel(),
                info.getSubway(),
                info.getImageUrl(),
                id.getLikes().size(),
                true
        );
    }
}
