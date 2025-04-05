package com.sm.seoulmate.api.batch;

import com.sm.seoulmate.domain.attraction.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("batch")
public class BatchController {

    private final BatchService batchService;

//    @Operation(summary = "관광명소.", description = "ci/cd swagger Test")
//    @GetMapping(value = "attraction")
//    public List<AttractionsItem> attraction() {
//        List<AttractionsItem> attractionsItems = new ArrayList<>();
//        Integer lastIndex = 0;
//        Integer startIndex = 1;
//        Integer endIndex = 1000;
//
//        do {
//            TestDto testDto = feignInterface.attraction(startIndex, endIndex);
//            lastIndex = testDto.getTbVwAttractions().getListTotalCount() + 1000;
//
//            attractionsItems.addAll(
//                    testDto.getTbVwAttractions().getRow().stream()
//                            .filter(x -> StringUtils.equals(x.getLangCodeId(), "ko")).toList()
//            );
//
//            startIndex += 1000;
//            endIndex += 1000;
//        } while (lastIndex > endIndex);
//
//        return attractionsItems;
//    }
}
