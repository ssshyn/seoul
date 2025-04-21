package com.sm.seoulmate.domain.attraction.dto;

import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchChallenge {
    @Schema(description = "챌린지 id", example = "3")
    Long id;
    @Schema(description = "챌린지 이름", example = "서촌 골목의 하루")
    String name;
    @Schema(description = "챌린지 타이틀", example = "통인시장부터 청와대 앞길까지")
    String title;
    @Schema(description = "챌린지 테마명", example = "지역 탐방")
    String challengeTheme;

    public SearchChallenge(Challenge challenge, LanguageCode languageCode) {
        boolean isKorean = Objects.equals(languageCode, LanguageCode.KOR);
        this.id = challenge.getId();
        this.name = isKorean ? challenge.getName() : challenge.getNameEng();
        this.title = isKorean ? challenge.getTitle() : challenge.getTitleEng();
        this.challengeTheme = isKorean ? challenge.getChallengeTheme().getNameKor() : challenge.getChallengeTheme().getNameEng();
    }
}
