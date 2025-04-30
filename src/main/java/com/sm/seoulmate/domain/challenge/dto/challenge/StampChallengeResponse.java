package com.sm.seoulmate.domain.challenge.dto.challenge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StampChallengeResponse {
    @Schema(description = "구분값(MISSED:놓치고있는챌린지, CHALLENGE:도전가능, PLACE:마지막관광지)")
    private String dataCode;
    @Schema(description = "챌린지 목록")
    private List<ChallengeResponse> challenges;
}
