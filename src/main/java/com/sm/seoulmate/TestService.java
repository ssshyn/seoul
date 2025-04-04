package com.sm.seoulmate;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sm.seoulmate.dto.NaverLocalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    @Value("${spring.cloud.gcp.credentials.location}")
    String jsonLocation;
    @Value("${naver.clientId}")
    String naverClientId;
    @Value("${naver.clientSecret}")
    String naverClientSecret;

    private final FeignInterface feignInterface;
    private final NaverFeignInterface naverFeignInterface;

    public String getTranslate(String text) throws Exception {
//        // 서비스 계정 JSON 파일을 사용하여 인증
//        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonLocation))
//                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//
//        Translate translate = TranslateOptions.newBuilder()
//                .setCredentials(credentials)
//                .build()
//                .getService();
//
//        // 번역 예시
//        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("en"));
//        return translation.getTranslatedText();
        return text;
    }

    public NaverLocalResponse getNaver(String text) {
        return naverFeignInterface.getNaverLocal(naverClientId, naverClientSecret, text, 100, 1, "random");
    }

    public NaverLocalResponse getNaverEnc(String text) {
        return naverFeignInterface.getNaverEnc(naverClientId, naverClientSecret, text, 100, 1);
    }
}
