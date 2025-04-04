package com.sm.seoulmate;

import com.sm.seoulmate.config.OpenFeignConfig;
import com.sm.seoulmate.dto.NaverLocalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverFeignInterface", url = "${naver.url}", configuration = OpenFeignConfig.class)
public interface NaverFeignInterface {
    @GetMapping("/v1/search/local.json")
    NaverLocalResponse getNaverLocal(@RequestHeader("X-Naver-Client-Id") String clientId,
                                     @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                     @RequestParam("query") String query,
                                     @RequestParam("display") int display,
                                     @RequestParam("start") int start,
                                     @RequestParam("sort") String sort);

    @GetMapping("/v1/search/encyc.json")
    NaverLocalResponse getNaverEnc(@RequestHeader("X-Naver-Client-Id") String clientId,
                                     @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                     @RequestParam("query") String query,
                                     @RequestParam("display") int display,
                                     @RequestParam("start") int start);
}

