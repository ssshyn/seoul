package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.ChallengeLikedResponse;
import com.sm.seoulmate.domain.challenge.dto.ChallengeUpdateResponse;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.challenge.enumeration.MyChallengeCode;
import com.sm.seoulmate.domain.challenge.service.ChallengeService;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "403", description = "BAD REQUEST", content = @Content(
                mediaType = "application/json",
                examples = {
                        @ExampleObject(name = "A0010", description = "만료된 엑세스 토큰입니다.",
                                value = """
                                            {"code": "A0010", "message": "만료된 엑세스 토큰입니다."}
                                            """)
                }, schema = @Schema(implementation = ErrorResponse.class)
        ))
})
@RestController
@RequestMapping("challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 전체조회 - 페이징", description = "키워드검색 (챌린지명)")
    @GetMapping
    public ResponseEntity<Page<ChallengeResponsess>> getChallenge(@ParameterObject Pageable pageable,
                                                                  @ParameterObject ChallengeSearchCondition challengeSearchCondition) {
        return ResponseEntity.ok(challengeService.getChallenge(challengeSearchCondition, pageable));
    }

    @Operation(summary = "MY - 나의 챌린지", description = "나의 챌린지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "USER_NOT_FOUND", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0002", description = "존재하지 않는 유저입니다.",
                                    value = """
                                            {"code": "U0002", "message": "존재하지 않는 유저입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "403", description = "LOGIN_NOT_ACCESS", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0001", description = "로그인이 필요한 서비스입니다. 로그인을 해주세요.",
                                    value = """
                                            {"code": "U0001", "message": "로그인이 필요한 서비스입니다. 로그인을 해주세요."}
                                            """),
                            @ExampleObject(name = "A0010", description = "만료된 엑세스 토큰입니다.",
                                    value = """
                                            {"code": "A0010", "message": "만료된 엑세스 토큰입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @GetMapping("/my")
    public ResponseEntity<List<ChallengeResponse>> myChallenge(@RequestParam("myChallenge") MyChallengeCode myChallengeCode,
                                                               @RequestParam("language") LanguageCode languageCode) {
        return ResponseEntity.ok(challengeService.myChallenge(languageCode, myChallengeCode));
    }

    @Operation(summary = "MY - 뱃지 목록 조회", description = "MY - 뱃지 목록 조회")
    @GetMapping("/my/badge")
    public ResponseEntity<List<ChallengeBadgeResponse>> myBadge(@RequestParam("language") LanguageCode languageCode) {
        return ResponseEntity.ok(challengeService.getBadgeStatus(languageCode));
    }

    @Operation(summary = "챌린지 상세조회", description = "챌린지 상세조회")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0002", description = "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0002", "message": "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "401", description = "USER_NOT_FOUND", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0002", description = "존재하지 않는 유저입니다.",
                                    value = """
                                            {"code": "U0002", "message": "존재하지 않는 유저입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChallenegeDetailResponse> getDetail(@RequestParam("language") LanguageCode languageCode,
                                                              @PathVariable("id") Long id) throws BadRequestException{
        return ResponseEntity.ok(challengeService.getDetail(languageCode, id));
    }

    @Operation(summary = "챌린지 진행상태 변경", description = "챌린지 진행상태 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0002", description = "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0002", "message": "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0005", description = "챌린지 완료 처리가 불가합니다. 다시 확인해 주세요. (스탬프 미완료)",
                                    value = """
                                            {"code": "R0005", "message": "챌린지 완료 처리가 불가합니다. 다시 확인해 주세요."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "401", description = "USER_NOT_FOUND", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0002", description = "존재하지 않는 유저입니다.",
                                    value = """
                                            {"code": "U0002", "message": "존재하지 않는 유저입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "403", description = "LOGIN_NOT_ACCESS", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0001", description = "로그인이 필요한 서비스입니다. 로그인을 해주세요.",
                                    value = """
                                            {"code": "U0001", "message": "로그인이 필요한 서비스입니다. 로그인을 해주세요."}
                                            """),
                            @ExampleObject(name = "A0010", description = "만료된 엑세스 토큰입니다.",
                                    value = """
                                            {"code": "A0010", "message": "만료된 엑세스 토큰입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PutMapping("/status")
    public ResponseEntity<ChallengeStatusResponse> updateStatus(@RequestParam(name = "id") Long id, @RequestParam(name = "status") ChallengeStatusCode challengeStatusCode) throws BadRequestException {
        return ResponseEntity.ok(challengeService.updateStatus(id, challengeStatusCode));
    }

    @Operation(summary = "챌린지 좋아요 등록/취소", description = "챌린지 좋아요 등록/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0002", description = "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0002", "message": "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "401", description = "USER_NOT_FOUND", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0002", description = "존재하지 않는 유저입니다.",
                                    value = """
                                            {"code": "U0002", "message": "존재하지 않는 유저입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "403", description = "LOGIN_NOT_ACCESS", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "U0001", description = "로그인이 필요한 서비스입니다. 로그인을 해주세요.",
                                    value = """
                                            {"code": "U0001", "message": "로그인이 필요한 서비스입니다. 로그인을 해주세요."}
                                            """),
                            @ExampleObject(name = "A0010", description = "만료된 엑세스 토큰입니다.",
                                    value = """
                                            {"code": "A0010", "message": "만료된 엑세스 토큰입니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PutMapping("/like")
    public ResponseEntity<ChallengeLikedResponse> updateLiked(@RequestParam("id") Long id) throws BadRequestException {
        return ResponseEntity.ok(challengeService.updateLiked(id));
    }

    @Operation(summary = "챌린지 등록", description = "챌린지 신규 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0001", description = "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0001", "message": "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0003", description = "챌린지 테마 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0003", "message": "테마 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0004", description = "필수값이 입력되지 않았습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0004", "message": "필수값이 입력되지 않았습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0006", description = "파라미터 최대 값을 초과하였습니다.",
                                    value = """
                                            {"code": "R0006", "message": "파라미터 최대 값을 초과하였습니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PostMapping
    public ResponseEntity<ChallengeResponse> create(@RequestBody ChallengeCreateRequest request) throws BadRequestException {
        return ResponseEntity.ok(challengeService.create(request));
    }

    @Operation(summary = "챌린지 수정", description = "챌린지 내용 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0001", description = "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0001", "message": "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0003", description = "챌린지 테마 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0003", "message": "테마 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0004", description = "필수값이 입력되지 않았습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0004", "message": "필수값이 입력되지 않았습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "R0006", description = "파라미터 최대 값을 초과하였습니다.",
                                    value = """
                                            {"code": "R0006", "message": "파라미터 최대 값을 초과하였습니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PutMapping
    public ResponseEntity<ChallengeResponse> update(@RequestBody ChallengeUpdateRequest request) throws BadRequestException {
        return ResponseEntity.ok(challengeService.update(request));
    }

    @Operation(summary = "챌린지 삭제", description = "챌린지 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0002", description = "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0002", "message": "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ChallengeUpdateResponse> delete(@PathVariable(value = "id") Long id) throws BadRequestException{
        challengeService.deleteChallenge(id);
        return ResponseEntity.ok(new ChallengeUpdateResponse(id, true));
    }

    @Operation(summary = "챌린지 테마 조회", description = "챌린지 테마 조회")
    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
            mediaType = "application/json",
            examples = {
                    @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                            value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
            }, schema = @Schema(implementation = ErrorResponse.class)
    ))
    @GetMapping("/theme")
    public ResponseEntity<List<ChallengeThemeResponse>> getChallengeTheme() {
        return ResponseEntity.ok(challengeService.getChallengeTheme());
    }

    @Operation(summary = "챌린지 테마 등록", description = "챌린지 테마 등록")
    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
            mediaType = "application/json",
            examples = {
                    @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                            value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
            }, schema = @Schema(implementation = ErrorResponse.class)
    ))
    @PostMapping("/theme")
    public ResponseEntity<ChallengeThemeResponse> createChallengeTheme(@RequestBody ChallengeThemeCreateRequest request) {
        return ResponseEntity.ok(challengeService.createTheme(request));
    }

    @Operation(summary = "챌린지 테마 삭제", description = "챌린지 테마 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0003", description = "챌린지 테마 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0003", "message": "테마 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @DeleteMapping("/theme/{id}")
    public ResponseEntity<ChallengeUpdateResponse> deleteChallengeTheme(@PathVariable(value = "id") Long id) throws BadRequestException{
        challengeService.deleteChallengeTheme(id);
        return ResponseEntity.ok(new ChallengeUpdateResponse(id, true));
    }
}
