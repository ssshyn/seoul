package com.sm.seoulmate.domain.login.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sm.seoulmate.domain.login.entity.User;
import com.sm.seoulmate.domain.login.enumeration.LoginType;
import com.sm.seoulmate.domain.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//
//    public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance())
//                .setAudience(Collections.singletonList("222259160589-9s3a5o54ur0ebk8pnvpoch2aj52p8t2m.apps.googleusercontent.com"))
//                .build();
//
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (Objects.isNull(idToken)) {
//            throw new RuntimeException("Invalid id token");
//        }
//
//        // payload 로 사용자 정보 가져오기
//        GoogleIdToken.Payload payload = idToken.getPayload();
//        String email = payload.getEmail();
//        String name = (String) payload.get("name");
//        String picture = (String) payload.get("name");
//
//        // jwt 토큰 발급
////        String jwt = jwtService.createToken(email);
////        ResponseEntity.ok(new AuthResponse(jwt));
//        return payload;
//    }

    public String processLogin(LoginType loginType, String token) {
        String email = "";

        switch (loginType) {
            case GOOGLE -> email = verifyGoogleToken(token);
            case FACEBOOK -> email = verifyFacebookToken(token);
            case APPLE -> email = verifyAppleToken(token);
            default -> throw new IllegalArgumentException("Unsupported login type");
        }

        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        // 회원가입 or 로그인 처리
        String finalEmail = email;
        User user = userRepository.findById(email)
                .orElseGet(() -> userRepository.save(new User(finalEmail, loginType)));

        return jwtUtil.generateAccessToken(user.getUserId());
    }

    private String verifyGoogleToken(String idToken) {
        // Google ID Token 검증
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("222259160589-9s3a5o54ur0ebk8pnvpoch2aj52p8t2m.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                return googleIdToken.getPayload().getEmail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String verifyFacebookToken(String accessToken) {
        // 페이스북 Graph API 통해 검증 (https://graph.facebook.com/me?access_token=...)
        // 이메일 추출해서 반환
        return null; // 생략
    }

    private String verifyAppleToken(String idToken) {
        // Apple Public Key로 JWT 디코딩 → 이메일 추출
        return null; // 생략
    }
}
