package com.sm.seoulmate.domain.challenge.dto.challenge;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.challenge.entity.Challenge;

import java.util.List;

public record LocationChallengeData(List<Challenge> challenges, List<AttractionId> attractionIds, boolean isJongGak) {
}
