package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.dto.AttractionResponse;
import com.sm.seoulmate.domain.attraction.dto.AttractionSearchCondition;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관광지 Controller", description = "관광지 관리 API")
@RestController
@RequestMapping("attraction")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

//    @GetMapping("setData")
//    public void setData() throws Exception {
//        batchService.setAttractionData();
//    }

    @Operation(summary = "전체조회 - 페이징", description = "전체조회 페이징 처리")
    @GetMapping
    public Page<AttractionResponse> getAttractionListPage(
            @ParameterObject Pageable pageable,
            @ParameterObject AttractionSearchCondition attractionSearchCondition) {
        return attractionService.getAttractions(attractionSearchCondition, pageable);
    }

    @Operation(summary = "관광지 좋아요 등록/취소", description = "관광지 좋아요 등록/취소")
    @PutMapping("/like/{id}")
    public ResponseEntity<Boolean> updateLiked(@PathVariable("id") Long id) {
        return ResponseEntity.ok(attractionService.updateLike(id));
    }

    @Operation(summary = "관광지 스탬프 등록", description = "관광지 스탬프 등록")
    @PostMapping("/stamp/{id}")
    public ResponseEntity<?> saveStamp(@PathVariable("id") Long id) {
        attractionService.saveStamp(id);
        return ResponseEntity.ok().build();
    }
}
