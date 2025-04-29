package com.sm.seoulmate.domain.attraction.feign;

import com.sm.seoulmate.config.OpenFeignConfig;
import com.sm.seoulmate.domain.attraction.feign.dto.SeoulDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tvSeoulMetroStoreInfoClient", url = "${seoul.url}", configuration = OpenFeignConfig.class)
public interface FeignInterface {

    @GetMapping("/${seoul.key}/json/{apiCode}/{startIndex}/{endIndex}")
    SeoulDataResponse<?> getSeoulData(@PathVariable("apiCode") String apiCode,
                                @PathVariable("startIndex") Integer startIndex,
                                 @PathVariable("endIndex") Integer endIndex);

    @GetMapping("/${seoul.key}/json/{apiCode}/{startIndex}/{endIndex}/{param}")
    SeoulDataResponse<?> getSeoulData(@PathVariable("apiCode") String apiCode,
                                      @PathVariable("startIndex") Integer startIndex,
                                      @PathVariable("endIndex") Integer endIndex,
                                      @PathVariable("param") String param);
}
