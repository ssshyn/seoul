package com.sm.seoulmate.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    @Schema(description = "유저ID")
    private Long id;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "로그인 타입")
    private LoginType loginType;
    @Schema(description = "신규 회원 여부")
    private Boolean isNewUser;
    @Schema(description = "access token")
    private String accessToken;
    @Schema(description = "refresh token")
    private String refreshToken;
}
