package com.sm.seoulmate.domain.attraction.dto;

import com.sm.seoulmate.domain.attraction.enumeration.AttractionDetailCode;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionRequest {
    @Schema(description = "attractionId")
    private Long attractionId;
    @Schema(description = "origin Key")
    private String originKey;
    @Schema(description = "소분류")
    private List<AttractionDetailCode> detailCodes;
    @Schema(description = "언어코드")
    private LanguageCode languageCode;
    @Schema(description = "장소명")
    private String name;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "x 좌표", example = "126.9798625")
    private String locationX;
    @Schema(description = "y 좌표", example = "37.5772571")
    private String locationY;
    @Schema(description = "홈페이지")
    private String hompageUrl;
    @Schema(description = "전화번호")
    private String tel;
    @Schema(description = "교통정보")
    private String subway;
    @Schema(description = "이미지")
    private String image;
}
