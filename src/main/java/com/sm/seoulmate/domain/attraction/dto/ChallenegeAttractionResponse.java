package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallenegeAttractionResponse {
        @Schema(description = "관광지 id")
        private Long id;
        @Schema(description = "관광지명")
        private String name;
        @Schema(description = "X 좌표")
        private String locationX;
        @Schema(description = "Y 좌표")
        private String locationY;
        @Schema(description = "주소")
        private String address;
        @Schema(description = "좋아요 여부")
        private Boolean isLiked;
        @Schema(description = "좋아요 수")
        private Integer likes;
        @Schema(description = "스탬프 여부")
        private Boolean isStamped;
        @Schema(description = "스탬프 찍은 사람 수")
        private Integer stampCount;
        @Schema(description = "이미지 url")
        private String imageUrl;
}
