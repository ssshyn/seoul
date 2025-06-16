package com.sm.seoulmate.domain.attraction;

import com.sm.seoulmate.domain.attraction.feign.NaverFeignInterface;
import com.sm.seoulmate.domain.attraction.feign.dto.NaverImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AttractionUtil {
    @Value("${naver.clientId}")
    private String naverClientId;
    @Value("${naver.clientSecret}")
    private String naverClientSecret;

    private final NaverFeignInterface naverFeignInterface;

    public AttractionUtil(NaverFeignInterface naverFeignInterface) {
        this.naverFeignInterface = naverFeignInterface;
    }

    // 관광지 이미지 조회
    public String getImageFromNaver(String keyword) {
        NaverImageResponse naverImageResponse = naverFeignInterface.getNaverImage(naverClientId, naverClientSecret, keyword, 1, 1);
        if (!naverImageResponse.getItems().isEmpty()) {
            NaverImageResponse.Item naverImage = naverImageResponse.getItems().get(0);

            return naverImage.getLink();
        }
        return null;
    }
}
