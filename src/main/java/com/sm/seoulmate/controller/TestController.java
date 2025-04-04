package com.sm.seoulmate.controller;

import com.sm.seoulmate.FeignInterface;
import com.sm.seoulmate.TestService;
import com.sm.seoulmate.dto.AttractionsItem;
import com.sm.seoulmate.dto.NaverLocalResponse;
import com.sm.seoulmate.api.attraction.response.SeoulDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final FeignInterface feignInterface;
    private final TestService testService;

    @Operation(summary = "테스트입니다.", description = "ci/cd swagger Test")
    @GetMapping
    public SeoulDataResponse test() {
        return feignInterface.market();
    }

    @Operation(summary = "테스트입니다.", description = "ci/cd swagger Test")
    @GetMapping(value = "attraction")
    public List<AttractionsItem> attraction() {
        List<AttractionsItem> attractionsItems = new ArrayList<>();
        Integer lastIndex = 0;
        Integer startIndex = 1;
        Integer endIndex = 1000;

        do {
            SeoulDataResponse seoulDataResponse = feignInterface.attraction(startIndex, endIndex);
            lastIndex = seoulDataResponse.getTbVwAttractions().getListTotalCount() + 1000;

            attractionsItems.addAll(
                    seoulDataResponse.getTbVwAttractions().getRow().stream()
                            .filter(x -> StringUtils.equals(x.getLangCodeId(), "ko")).toList()
            );

            startIndex += 1000;
            endIndex += 1000;
        } while (lastIndex > endIndex);

        return attractionsItems;
    }

    @GetMapping("translate")
    public String translate(@RequestParam(value = "text") String text) throws Exception{
        return testService.getTranslate(text);
    }
    @GetMapping("naver")
    public NaverLocalResponse naver(@RequestParam(value = "text") String text) {
        return testService.getNaver(text);
    }

    @GetMapping("naver-enc")
    public NaverLocalResponse naverEnc(@RequestParam(value = "text") String text) {
        return testService.getNaverEnc(text);
    }
    @PostMapping
    public void post1() {}
    @PutMapping
    public void put1() {}
    @DeleteMapping
    public void delete1() {}
}
