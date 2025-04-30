package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Level {
    EASY(1),
    NORMAL(2),
    HARD(3);

    private Integer levelNum;
}
