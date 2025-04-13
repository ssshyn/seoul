package com.sm.seoulmate.api.login;

import com.sm.seoulmate.api.login.request.LoginRequest;
import com.sm.seoulmate.api.login.response.LoginResponse;
import com.sm.seoulmate.domain.login.service.JwtUtil;
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
