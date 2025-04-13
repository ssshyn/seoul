package com.sm.seoulmate.domain.login.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sm.seoulmate.api.login.response.FacebookUserResponse;
import com.sm.seoulmate.domain.login.entity.User;
import com.sm.seoulmate.domain.login.enumeration.LoginType;
import com.sm.seoulmate.domain.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${apple.filePath}")
    private String appleKeyPath;
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

        //todo: test 데이터 지우기

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

    private String verifyGoogleToken(String accessToken) {
        // Google ID Token 검증
        if ("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaXNzIjoiYWNjb3VudHMuZ29vZ2xlLmNvbSIsImF1ZCI6InlvdXItY2xpZW50LWlkIiwiZXhwIjo5OTk5OTk5OTk5fQ.dummy-signature".equals(accessToken)) {
            // todo: 삭제 개발용 테스트 토큰 처리
            return "test@example.com";
        }
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("222259160589-9s3a5o54ur0ebk8pnvpoch2aj52p8t2m.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(accessToken);
            if (googleIdToken != null) {
                return googleIdToken.getPayload().getEmail();
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid Facebook token", e);
        }
        return null;
    }

    private String verifyFacebookToken(String accessToken) {
        if ("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaXNzIjoiYWNjb3VudHMuZ29vZ2xlLmNvbSIsImF1ZCI6InlvdXItY2xpZW50LWlkIiwiZXhwIjo5OTk5OTk5OTk5fQ.dummy-signature".equals(accessToken)) {
            // todo: 삭제 개발용 테스트 토큰 처리
            return "test@example.com";
        }

        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<FacebookUserResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, FacebookUserResponse.class);

            FacebookUserResponse user = response.getBody();
            if (user == null || user.getEmail() == null) {
                throw new RuntimeException("Failed to get user info from Facebook");
            }

            return user.getEmail(); // 로그인 식별용 이메일
        } catch (Exception e) {
            throw new RuntimeException("Invalid Facebook token", e);
        }
    }

    private String verifyAppleToken(String accessToken) {
//        if ("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaXNzIjoiYWNjb3VudHMuZ29vZ2xlLmNvbSIsImF1ZCI6InlvdXItY2xpZW50LWlkIiwiZXhwIjo5OTk5OTk5OTk5fQ.dummy-signature".equals(accessToken)) {
//            // todo: 삭제 개발용 테스트 토큰 처리
//            return "test@example.com";
//        }
        // Apple Public Key로 JWT 디코딩 → 이메일 추출
        try {
            // RSA 공개 키 로드
            RSAPublicKey publicKey = loadRSAPublicKeyFromFile();

            // JWT 디코딩 및 검증
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
            DecodedJWT decodedJWT = verifier.verify(accessToken);

            // JWT에서 이메일 추출
            String email = decodedJWT.getClaim("email").asString();
            return email;

        } catch (Exception e) {
            throw new RuntimeException("Invalid Facebook token", e);
        }
    }

    // RSA 퍼블릭 키 파일에서 키를 읽어오는 메소드
    private RSAPublicKey loadRSAPublicKeyFromFile() throws Exception {
        // 파일에서 공개 키를 읽어오기
        String keyContent = new String(Files.readAllBytes(Paths.get(appleKeyPath)));

        // `-----BEGIN PUBLIC KEY-----` 및 `-----END PUBLIC KEY-----`를 제외하고 Base64로 디코딩
        String publicKeyPEM = keyContent.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");

        // Base64 디코딩
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // 디코딩된 바이트 배열을 사용하여 RSAPublicKey 생성
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(decoded));
        return publicKey;
    }
}
