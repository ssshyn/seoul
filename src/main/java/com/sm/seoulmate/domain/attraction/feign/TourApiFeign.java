package com.sm.seoulmate.domain.attraction.feign;

import com.sm.seoulmate.config.OpenFeignConfig;
import com.sm.seoulmate.domain.attraction.feign.dto.tourApiDto.KeywordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourApiFeign", url = "${tour.url}", configuration = OpenFeignConfig.class)
public interface TourApiFeign {
    @GetMapping("/searchKeyword1?numOfRows=1&pageNo=1&MobileOS=ETC&MobileApp=AppTest&arrange=A&_type=json&areaCode=1&ServiceKey="+"${tour.key}")
    KeywordResponse searchKeyword(@RequestParam("keyword") String keyword);

    @GetMapping("/detailImage1?MobileOS=ETC&MobileApp=AppTest&imageYN=Y&subImageYN=Y&numOfRows=1&_type=json&ServiceKey="+"${tour.key}")
    KeywordResponse getTourImages(@RequestParam("contentId") String tourId);

    @GetMapping("/detailCommon1?MobileOS=ETC&MobileApp=AppTest&overviewYN=Y&_type=json&ServiceKey="+"${tour.key}")
    KeywordResponse getDescription(@RequestParam("contentId") String tourId,
                                  @RequestParam("contentTypeId") String tourTypeId);
}
