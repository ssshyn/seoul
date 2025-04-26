package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.dto.AttractionDetailResponse;
import com.sm.seoulmate.domain.attraction.dto.AttractionLikedResponse;
import com.sm.seoulmate.domain.attraction.dto.AttractionUpdateResponse;
import com.sm.seoulmate.domain.attraction.dto.SearchResponse;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관광지 Controller", description = "관광지 관리 API")
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
@RestController
@RequestMapping("attraction")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @Operation(summary = "전체검색", description = "전체검색 - 관광지, 챌린지")
    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam("language") LanguageCode languageCode) {
        return ResponseEntity.ok(attractionService.search(keyword, languageCode));
    }

    @Operation(summary = "관광지 상세 조회", description = "관광지 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0001", description = "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0001", "message": "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
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
    public ResponseEntity<AttractionDetailResponse> getDetail(@PathVariable("id") Long id, @RequestParam("language") LanguageCode languageCode) throws BadRequestException {
        return ResponseEntity.ok(attractionService.getDetail(id, languageCode));
    }

    @Operation(summary = "좋아요한 관광지 조회", description = "좋아요한 관광지 조회")
    @ApiResponses({
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
    @GetMapping("/my")
    public ResponseEntity<List<AttractionDetailResponse>> my(@ParameterObject Pageable pageable, @RequestParam("language") LanguageCode languageCode) {
        return ResponseEntity.ok(attractionService.my(pageable, languageCode));
    }


    @Operation(summary = "관광지 좋아요 등록/취소", description = "관광지 좋아요 등록/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0001", description = "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0001", "message": "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
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
    public ResponseEntity<AttractionLikedResponse> updateLiked(@RequestParam("id") Long id) {
        return ResponseEntity.ok(attractionService.updateLike(id));
    }

    @Operation(summary = "관광지 스탬프 등록", description = "관광지 스탬프 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0001", description = "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0001", "message": "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
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
    @PostMapping("/stamp")
    public ResponseEntity<AttractionUpdateResponse> saveStamp(@RequestParam("id") Long id) {
        attractionService.saveStamp(id);
        return ResponseEntity.ok(new AttractionUpdateResponse(id, true));
    }
}
