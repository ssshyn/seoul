package com.sm.seoulmate;

import com.sm.seoulmate.config.OpenFeignConfig;
import com.sm.seoulmate.api.attraction.response.SeoulDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tvSeoulMetroStoreInfoClient", url = "${seoul.url}", configuration = OpenFeignConfig.class)
public interface FeignInterface {
    @GetMapping("/${seoul.key}" + "/json/tvSeoulmetroStoreInfo/1/5")
    SeoulDataResponse market();

    @GetMapping("/${seoul.key}/json/TbVwAttractions/{startIndex}/{endIndex}")
    SeoulDataResponse attraction(@PathVariable("startIndex") Integer startIndex,
                                 @PathVariable("endIndex") Integer endIndex);

    @GetMapping("/${seoul.key}/json/viewNightSpot/{startIndex}/{endIndex}")
    SeoulDataResponse getNightView(@PathVariable("startIndex") Integer startIndex,
                                   @PathVariable("endIndex") Integer endIndex);
}
