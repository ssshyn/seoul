package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.challenge.service.ChallengeService;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "챌린지 Controller", description = "챌린지 관리 API")
@RestController
@RequestMapping("challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 전체조회 - 페이징", description = "키워드검색 (챌린지명)")
    @GetMapping
    public ResponseEntity<Page<ChallengeResponse>> getChallenge(@ParameterObject Pageable pageable,
                                                                @ParameterObject ChallengeSearchCondition challengeSearchCondition) {
        return ResponseEntity.ok(challengeService.getChallenge(challengeSearchCondition, pageable));
    }

    @Operation(summary = "챌린지 상세조회", description = "챌린지 상세조회")
    @GetMapping("/{id}")
    public ResponseEntity<ChallenegeDetailResponse> getDetail(@RequestParam("language") LanguageCode languageCode,
                                                              @PathVariable("id") Long id) throws BadRequestException{
        return ResponseEntity.ok(challengeService.getDetail(languageCode, id));
    }

    @Operation(summary = "챌린지 진행상태 변경", description = "챌린지 진행상태 변경")
    @PutMapping("/status")
    public ResponseEntity<ChallengeStatusResponse> updateStatus(@RequestParam(name = "id") Long id, @RequestParam(name = "status") ChallengeStatusCode challengeStatusCode) {
        return ResponseEntity.ok(challengeService.updateStatus(id, challengeStatusCode));
    }

    @Operation(summary = "챌린지 좋아요 등록/취소", description = "챌린지 좋아요 등록/취소")
    @PutMapping("/like")
    public ResponseEntity<Boolean> updateLiked(@RequestParam("id") Long id) {
        return ResponseEntity.ok(challengeService.updateLiked(id));
    }

    @Operation(summary = "챌린지 등록", description = "챌린지 신규 등록")
    @PostMapping
    public ResponseEntity<ChallengeResponse> create(@RequestBody ChallengeCreateRequest request) throws BadRequestException {
        return ResponseEntity.ok(challengeService.create(request));
    }

    @Operation(summary = "챌린지 수정", description = "챌린지 내용 수정")
    @PutMapping
    public ResponseEntity<ChallengeResponse> update(@RequestBody ChallengeUpdateRequest request) throws BadRequestException {
        return ResponseEntity.ok(challengeService.update(request));
    }

    @Operation(summary = "챌린지 삭제", description = "챌린지 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws BadRequestException{
        challengeService.deleteChallenge(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "챌린지 테마 조회", description = "챌린지 테마 조회")
    @GetMapping("/theme")
    public ResponseEntity<List<ChallengeThemeResponse>> getChallengeTheme() {
        return ResponseEntity.ok(challengeService.getChallengeTheme());
    }

    @Operation(summary = "챌린지 테마 등록", description = "챌린지 테마 등록")
    @PostMapping("/theme")
    public ResponseEntity<ChallengeThemeResponse> createChallengeTheme(@RequestBody ChallengeThemeCreateRequest request) {
        return ResponseEntity.ok(challengeService.createTheme(request));
    }

    @Operation(summary = "챌린지 테마 삭제", description = "챌린지 테마 삭제")
    @DeleteMapping("/theme/{id}")
    public ResponseEntity<?> deleteChallengeTheme(@PathVariable(value = "id") Long id) throws BadRequestException{
        challengeService.deleteChallengeTheme(id);
        return ResponseEntity.ok().build();
    }
}
