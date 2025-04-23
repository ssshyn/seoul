package com.sm.seoulmate.domain.attraction.dto;

import com.sm.seoulmate.domain.attraction.enumeration.AttractionDetailCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AttractionDetailResponse(
        @Schema(description = "관광지 id")
        Long id,
        @Schema(description = "관광지명")
        String name,
        @Schema(description = "관광지 설명")
        String description,
        @Schema(description = "관광지 분류")
        List<AttractionDetailCode> detailCodes,
        @Schema(description = "관광지 주소")
        String address,
        @Schema(description = "영업시간")
        String businessHours,
        @Schema(description = "홈페이지 url")
        String homepageUrl,
        @Schema(description = "X 좌표")
        String locationX,
        @Schema(description = "Y 좌표")
        String locationY,
        @Schema(description = "전화번호")
        String tel,
        @Schema(description = "교통정보")
        String subway,
        @Schema(description = "이미지 url")
        String imageUrl,
        @Schema(description = "좋아요 수")
        Integer likes,
        @Schema(description = "좋아요 여부")
        Boolean isLiked,
        @Schema(description = "스탬프 찍은 사람 수")
        Integer stampCount
) {
}
