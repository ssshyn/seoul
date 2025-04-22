package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.dto.AttractionDetailResponse;
import com.sm.seoulmate.domain.attraction.dto.SearchResponse;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
import com.sm.seoulmate.domain.attraction.service.BatchService;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관광지 Controller", description = "관광지 관리 API")
@RestController
@RequestMapping("attraction")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;
    private final BatchService batchService;

    @GetMapping("setData")
    public void setData() throws Exception {
        batchService.setAttractionData();
    }

    @Operation(summary = "전체검색", description = "전체검색 - 관광지, 챌린지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam("language") LanguageCode languageCode) {
        return ResponseEntity.ok(attractionService.search(keyword, languageCode));
    }

    @Operation(summary = "관광지 상세 조회", description = "관광지 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AttractionDetailResponse> getDetail(@PathVariable("id") Long id, @RequestParam("language") LanguageCode languageCode) throws BadRequestException {
        return ResponseEntity.ok(attractionService.getDetail(id, languageCode));
    }

//    @Operation(summary = "좋아요한 관광지 조회", description = "좋아요한 관광지 조회")
//    @GetMapping("/my")
//    public ResponseEntity<Page<AttractionDetailResponse>> my(@ParameterObject Pageable pageable, @RequestParam("language") LanguageCode languageCode) {
//        return ResponseEntity.ok(attractionService.my(pageable, languageCode));
//    }


    @Operation(summary = "관광지 좋아요 등록/취소", description = "관광지 좋아요 등록/취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @PutMapping("/like")
    public ResponseEntity<Boolean> updateLiked(@RequestParam("id") Long id) {
        return ResponseEntity.ok(attractionService.updateLike(id));
    }

    @Operation(summary = "관광지 스탬프 등록", description = "관광지 스탬프 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @PostMapping("/stamp")
    public ResponseEntity<?> saveStamp(@RequestParam("id") Long id) {
        attractionService.saveStamp(id);
        return ResponseEntity.ok().build();
    }
}
