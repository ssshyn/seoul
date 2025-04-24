package com.sm.seoulmate.domain.user.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
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
import com.sm.seoulmate.domain.user.dto.TokenRefreshRequest;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import com.sm.seoulmate.domain.user.enumeration.NicknamePrefix;
import com.sm.seoulmate.domain.user.enumeration.NicknameSuffix;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
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

        switch (loginType) {
            case GOOGLE -> email = verifyGoogleToken(token);
            case FACEBOOK -> email = verifyFacebookToken(token);
            case APPLE -> email = verifyAppleToken(token);
            default -> throw new ErrorException(ErrorCode.NOT_MATCH_CATEGORY);
        }

        if (StringUtils.trimToEmpty(email).isEmpty()) {
            throw new ErrorException(ErrorCode.UNAUTHENTICATED_USER);
        }

        // 회원가입 or 로그인 처리
        String finalEmail = email;
        Boolean isNewUser = false;
        Map<String, String> nicknameMap = makeNickname();
        Optional<User> userOptional = userRepository.findByEmailAndLoginType(email, loginType);
        User user;
        if(userOptional.isEmpty()) {
            isNewUser = true;
            user = userRepository.save(User.of(finalEmail, loginType, nicknameMap.get("kor"), nicknameMap.get("eng")));
        } else {
            user = userOptional.get();
        }

        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(Objects.equals(languageCode, LanguageCode.KOR) ? user.getNicknameKor() : user.getNicknameEng())
                .loginType(user.getLoginType())
                .isNewUser(isNewUser)
                .accessToken(jwtUtil.generateAccessToken(user.getId().toString()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getId().toString()))
                .build();
    }

    public LoginResponse refreshAccessToken(TokenRefreshRequest request) {
        return jwtUtil.refreshAccessToken(StringUtils.trimToEmpty(request.getRefreshToken()));
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
                    .setAudience(
                            Arrays.asList("222259160589-9s3a5o54ur0ebk8pnvpoch2aj52p8t2m.apps.googleusercontent.com",
                                    "222259160589-qrilqthr6njluafs3inq3lan0mrikp3u.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(accessToken);
            if (googleIdToken != null) {
                return googleIdToken.getPayload().getEmail();
            }
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.NOT_MATCH_CATEGORY);
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
            if (Objects.isNull(user)) {
                System.out.println("logger > error : { facebook user not found }");
                throw new ErrorException(ErrorCode.UNAUTHENTICATED_USER);
            }

            return user.getEmail(); // 로그인 식별용 이메일
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.INVALID_TOKEN);
        }
    }
//
//    public String exchangeFacebookToken(String authorizationCode) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://graph.facebook.com/v22.0/oauth/access_token";
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("client_id", "1873164406770276");
//        params.add("client_secret", "1d006ef5ca9b6a212ce931510bc2d7b1");
////        params.add("grant_type", "fb_exchange_token");
////        params.add("fb_exchange_token", authorizationCode);
//        params.add("redirect_uri", "fb1873164406770276://authorize"); // iOS 설정과 일치해야 함
//        params.add("code", authorizationCode);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        ResponseEntity<FacebookTokenResponse> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                request,
//                FacebookTokenResponse.class
//        );
//
//        FacebookTokenResponse facebookTokenResponse = response.getBody(); // access_token, token_type 등 포함
//
//        if(Objects.isNull(facebookTokenResponse)) {
//            throw new RuntimeException("Empty Response to Facebook");
//        }
//        if(Strings.isNullOrEmpty(facebookTokenResponse.getAccess_token())) {
//            throw new RuntimeException("Invalid IOS FACEBOOK TOKEN");
//        }
//
//        return verifyFacebookToken(StringUtils.trimToEmpty(facebookTokenResponse.getAccess_token()));
//    }

    private String verifyAppleToken(String accessToken) {
        if ("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaXNzIjoiYWNjb3VudHMuZ29vZ2xlLmNvbSIsImF1ZCI6InlvdXItY2xpZW50LWlkIiwiZXhwIjo5OTk5OTk5OTk5fQ.dummy-signature".equals(accessToken)) {
            // todo: 삭제 개발용 테스트 토큰 처리
            return "test@example.com";
        }
        // Apple Public Key로 JWT 디코딩 → 이메일 추출
        try {
            RSAPublicKey applePublicKey = fetchApplePublicKey(accessToken);

            // 2. JWT 검증
            Algorithm algorithm = Algorithm.RSA256(applePublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(accessToken);

            // JWT에서 이메일 추출
            return decodedJWT.getClaim("email").asString();

        } catch (Exception e) {
            throw new ErrorException(ErrorCode.INVALID_TOKEN);
        }
    }

    // RSA 퍼블릭 키 파일에서 키를 읽어오는 메소드
    private ECPrivateKey loadRSAPublicKeyFromFile() throws Exception {

        // 파일에서 공개 키를 읽어오기
        String keyContent = new String(Files.readAllBytes(Paths.get(appleKeyPath)));

        // `-----BEGIN PUBLIC KEY-----` 및 `-----END PUBLIC KEY-----`를 제외하고 Base64로 디코딩
        String publicKeyPEM = keyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static RSAPublicKey generatePublicKeyFromPrivateKey(PrivateKey privateKey) throws Exception {
        if (privateKey instanceof RSAPrivateKey) {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;

            // 개인 키의 Modulus (N)과 Public Exponent (e)를 사용하여 공개 키 생성
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), BigInteger.valueOf(65537)); // 공개 지수는 일반적으로 65537
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } else {
            throw new IllegalArgumentException("The provided key is not an RSA private key");
        }
    }

    private RSAPublicKey fetchApplePublicKey(String idToken) {
        try {
            // 1. JWT header에서 key id(kid), alg 가져오기
            DecodedJWT decodedJWT = JWT.decode(idToken);
            String kid = decodedJWT.getKeyId();
            String alg = decodedJWT.getAlgorithm();

            // 2. Apple 공개키 요청
            URL url = new URL("https://appleid.apple.com/auth/keys");
            InputStream inputStream = url.openStream();
            String jwksJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            JwkProvider provider = new UrlJwkProvider(new URL("https://appleid.apple.com/auth/keys"));
            Jwk jwk = provider.get(kid); // kid에 해당하는 공개키 가져오기

            return (RSAPublicKey) jwk.getPublicKey();
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.KEY_PARSING_ERROR);
        }
    }
}
