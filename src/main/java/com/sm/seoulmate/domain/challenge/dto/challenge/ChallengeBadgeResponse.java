package com.sm.seoulmate.domain.challenge.dto.challenge;

public record ChallengeBadgeResponse (
        Long themeId,
        String themeName,
        Integer themeCount,
        Integer completeCount,
        Boolean isCompleted
){
}
