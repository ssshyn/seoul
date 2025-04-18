package com.sm.seoulmate.domain.attraction.feign;

import com.sm.seoulmate.config.OpenFeignConfig;
import com.sm.seoulmate.domain.attraction.feign.dto.NaverMapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverFeign", url = "${naver.map.url}", configuration = OpenFeignConfig.class)
public interface NaverFeign {
    @GetMapping("/v2/geocode")
    NaverMapResponse getCoordinate(@RequestHeader("x-ncp-apigw-api-key-id") String clientId,
                                   @RequestHeader("x-ncp-apigw-api-key") String clientSecret,
                                   @RequestHeader("Accept") String responseType,
                                   @RequestParam("query") String address);
}
