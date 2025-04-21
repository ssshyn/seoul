package com.sm.seoulmate.domain.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sm.seoulmate.domain.user.dto.FacebookUserResponse;
import com.sm.seoulmate.domain.user.dto.LoginRequest;
import com.sm.seoulmate.domain.user.dto.LoginResponse;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import com.sm.seoulmate.domain.user.enumeration.NicknamePrefix;
import com.sm.seoulmate.domain.user.enumeration.NicknameSuffix;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${apple.filePath}")
    private String appleKeyPath;

    public Map<String, String> makeNickname() {
        Map<String, String> nicknameMap = new HashMap<>();
        String nicknameKor;
        String nicknameEng;
        boolean isPresent;

        do {
            NicknamePrefix prefix = getRandomEnumValue(NicknamePrefix.class);
            NicknameSuffix suffix = getRandomEnumValue(NicknameSuffix.class);

            nicknameKor = prefix.getKor() + " " + suffix.getKor();
            nicknameEng = prefix.getEng() + " " + suffix.getEng();

            isPresent = userRepository.findByNicknameKor(nicknameKor).isPresent();
        } while (isPresent);

        nicknameMap.put("kor", nicknameKor);
        nicknameMap.put("eng", nicknameEng);
        return nicknameMap;
    }

    private static <T extends Enum<?>> T getRandomEnumValue(Class<T> clazz) {
        T[] enumConstants = clazz.getEnumConstants();
        return enumConstants[ThreadLocalRandom.current().nextInt(enumConstants.length)];
    }

    public LoginResponse processLogin(LoginRequest condition) {
        LoginType loginType = condition.getLoginType();
        String token = StringUtils.trimToEmpty(condition.getToken());
        LanguageCode languageCode = condition.getLanguageCode();
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
        Map<String, String> nicknameMap = makeNickname();
        User user = userRepository.findById(email)
                .orElseGet(() -> userRepository.save(User.of(finalEmail, loginType, nicknameMap.get("kor"), nicknameMap.get("eng"))));

        return LoginResponse.builder()
                .email(user.getUserId())
                .nickname(Objects.equals(languageCode, LanguageCode.KOR) ? user.getNicknameKor() : user.getNicknameEng())
                .accessToken(jwtUtil.generateAccessToken(user.getUserId()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getUserId()))
                .build();
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtUtil.refreshAccessToken(refreshToken);
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
        if ("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaXNzIjoiYWNjb3VudHMuZ29vZ2xlLmNvbSIsImF1ZCI6InlvdXItY2xpZW50LWlkIiwiZXhwIjo5OTk5OTk5OTk5fQ.dummy-signature".equals(accessToken)) {
            // todo: 삭제 개발용 테스트 토큰 처리
            return "test@example.com";
        }
        // Apple Public Key로 JWT 디코딩 → 이메일 추출
        try {
            // RSA 공개 키 로드
            RSAPublicKey publicKey = loadRSAPublicKeyFromFile();

            // JWT 디코딩 및 검증
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
            DecodedJWT decodedJWT = verifier.verify(accessToken);

            // JWT에서 이메일 추출
            return decodedJWT.getClaim("email").asString();

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
        return (RSAPublicKey) keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(decoded));
    }
}
