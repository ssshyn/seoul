package com.sm.seoulmate.domain.challenge.dto.challenge;

import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChallengeStatusResponse(
        @Schema(description = "챌린지 id", example = "3")
        Long id,
        @Schema(description = "챌린지 진행 상태", example = "PROGRESS")
        ChallengeStatusCode challengeStatusCode
) {
}
