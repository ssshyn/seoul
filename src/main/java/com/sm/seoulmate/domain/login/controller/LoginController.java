package com.sm.seoulmate.domain.login.controller;

import com.sm.seoulmate.domain.login.dto.LoginRequest;
import com.sm.seoulmate.domain.login.dto.LoginResponse;
import com.sm.seoulmate.util.JwtUtil;
import com.sm.seoulmate.domain.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    @GetMapping("/nick")
    public ResponseEntity<String> nick(@RequestParam(value = "code") String code) {
        return ResponseEntity.ok(loginService.makeNickname(code));
    }

    @Operation(
            summary = "로그인",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse jwt = loginService.processLogin(request.getLoginType(), request.getToken());
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
