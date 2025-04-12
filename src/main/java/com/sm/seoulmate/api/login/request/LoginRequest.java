package com.sm.seoulmate.api.login.request;

import com.sm.seoulmate.domain.login.enumeration.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Schema(description = "로그인 토큰")
    private String token;
    @Schema(description = "로그인 타입")
    private LoginType loginType;
}
