package com.sm.seoulmate.domain.attraction.dto;

import com.sm.seoulmate.domain.attraction.enumeration.AttractionDetailCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttractionDetailResponse {
        @Schema(description = "관광지 id")
        private Long id;
        @Schema(description = "관광지명")
        private String name;
        @Schema(description = "관광지 설명")
        private String description;
        @Schema(description = "관광지 분류")
        private List<AttractionDetailCode> detailCodes;
        @Schema(description = "관광지 주소")
        private String address;
        @Schema(description = "영업시간")
        private String businessHours;
        @Schema(description = "홈페이지 url")
        private String homepageUrl;
        @Schema(description = "X 좌표")
        private String locationX;
        @Schema(description = "Y 좌표")
        private String locationY;
        @Schema(description = "전화번호")
        private String tel;
        @Schema(description = "교통정보")
        private String subway;
        @Schema(description = "이미지 url")
        private String imageUrl;
        @Schema(description = "좋아요 수")
        private Integer likes;
        @Schema(description = "좋아요 여부")
        private Boolean isLiked;
        @Schema(description = "스탬프 찍은 사람 수")
        private Integer stampCount;
}
