package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CulturalChallenge {
    @Schema(description = "챌린지 id", example = "3")
    private Long id;
    @Schema(description = "챌린지 이름", example = "지금 열린 벚꽃 축제")
    private String title;
    @Schema(description = "이미지 url")
    private String imageUrl;
    @Schema(description = "시작일자")
    private LocalDate startDate;
    @Schema(description = "종료일자")
    private LocalDate endDate;
    @Schema(description = "홈페이지 url")
    private String hompageUrl;
    @Schema(description = "찜 여부")
    private Boolean isLiked;
}
