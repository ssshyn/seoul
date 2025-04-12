package com.sm.seoulmate.api.login;

import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.sm.seoulmate.api.login.request.LoginRequest;
import com.sm.seoulmate.domain.login.service.JwtUtil;
import com.sm.seoulmate.domain.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    //http://localhost:8081/login/oauth2/code/google
    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    @Operation(
            summary = "로그인",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String jwt = loginService.processLogin(request.getLoginType(), request.getToken());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        try {
            String userId = jwtUtil.parseToken(refreshToken).getSubject();
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            return ResponseEntity.ok(newAccessToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh Token");
        }
    }
}
