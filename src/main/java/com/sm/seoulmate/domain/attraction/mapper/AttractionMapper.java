package com.sm.seoulmate.domain.attraction.mapper;

import com.sm.seoulmate.domain.attraction.dto.AttractionResponse;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;

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
                entity.getLikes()
        );
    }
}
