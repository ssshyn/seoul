package com.sm.seoulmate.domain.user.controller;

import com.sm.seoulmate.domain.user.dto.LoginRequest;
import com.sm.seoulmate.domain.user.dto.LoginResponse;
import com.sm.seoulmate.util.JwtUtil;
import com.sm.seoulmate.domain.user.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 Controller", description = "인증 관리 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @Operation(
            summary = "로그인",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest condition) {
        LoginResponse jwt = loginService.processLogin(condition);
        return ResponseEntity.ok(jwt);
    }

    @Operation(
            summary = "리프레시 토큰",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam(value = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(loginService.refreshAccessToken(refreshToken));
    }
}
