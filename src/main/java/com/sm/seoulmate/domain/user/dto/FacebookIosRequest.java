package com.sm.seoulmate.domain.user.dto;

import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacebookIosRequest {
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "언어")
    private LanguageCode languageCode;
}
