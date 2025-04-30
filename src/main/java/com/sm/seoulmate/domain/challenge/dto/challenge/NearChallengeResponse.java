package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearChallengeResponse {
    @Schema(description = "기본값(종각)여부")
    private boolean isJongGak;
    @Schema(description = "근처 챌린지 목록", example = "서촌 골목의 하루")
    private List<ChallengeResponse> challenges;
}
