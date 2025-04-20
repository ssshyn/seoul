package com.sm.seoulmate.domain.attraction.mapper;

import com.sm.seoulmate.domain.attraction.dto.AttractionResponse;
import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.util.UserInfoUtil;
import org.apache.commons.lang3.StringUtils;

public class AttractionMapper {
    public static AttractionResponse toResponse(AttractionId entity) {
        AttractionInfo info = entity.getAttractionInfos().get(0);
        return new AttractionResponse(
                entity.getId(),
                info.getName(),
                info.getDescription(),
                entity.getAttractionDetailCodes(),
                info.getAddress(),
                info.getLocationX(),
                info.getLocationY(),
                info.getTel(),
                info.getSubway(),
                info.getImageUrl(),
                entity.getLikes().size()
        );
    }

    public static ChallenegeAttractionResponse toChallengeResponse(AttractionId entity, LanguageCode languageCode) {
        AttractionInfo info = entity.getAttractionInfos().stream().filter(attractionInfo -> attractionInfo.getLanguageCode().equals(languageCode)).findFirst().orElse(new AttractionInfo());

        return new ChallenegeAttractionResponse(
                entity.getId(),
                info.getName(),
                info.getLocationX(),
                info.getLocationY(),
                entity.getLikes().stream().anyMatch(like -> StringUtils.equals(like.getUser().getUserId(), UserInfoUtil.getUserId())),
                entity.getLikes().size(),
                entity.getVisitStamps().stream().anyMatch(stamp -> StringUtils.equals(stamp.getUser().getUserId(), UserInfoUtil.getUserId())),
                entity.getVisitStamps().size()
        );
    }

    public static AttractionResponse toLikesResponse(AttractionLikes entity, LanguageCode languageCode) {
        AttractionId id = entity.getAttraction();
        AttractionInfo info = id.getAttractionInfos().stream().filter(attractionInfo -> attractionInfo.getLanguageCode().equals(languageCode)).findFirst().orElse(new AttractionInfo());

        return new AttractionResponse(
                id.getId(),
                info.getName(),
                info.getDescription(),
                id.getAttractionDetailCodes(),
                info.getAddress(),
                info.getLocationX(),
                info.getLocationY(),
                info.getTel(),
                info.getSubway(),
                info.getImageUrl(),
                id.getLikes().size()
        );
    }
}
