package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DisplayRank {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CULTURE(9);

    private Integer rankNum;
}
