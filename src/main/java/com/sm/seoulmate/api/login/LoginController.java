package com.sm.seoulmate.api.login;

import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.sm.seoulmate.api.login.request.LoginRequest;
import com.sm.seoulmate.domain.login.service.JwtUtil;
import com.sm.seoulmate.domain.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    //http://localhost:8081/login/oauth2/code/google
    private final JwtUtil jwtUtil;
    private final LoginService loginService;

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
