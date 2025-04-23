package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyChallengeCode {
    LIKE(null),
    PROGRESS(ChallengeStatusCode.PROGRESS),
    COMPLETE(ChallengeStatusCode.COMPLETE);

    private final ChallengeStatusCode challengeStatusCode;
}
