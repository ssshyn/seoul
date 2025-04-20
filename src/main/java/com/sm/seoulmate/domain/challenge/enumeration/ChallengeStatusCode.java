package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChallengeStatusCode {
    PROGRESS("진행중"),
    COMPLETE("완료");

    private final String description;
}
