package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.ChallengeUpdateResponse;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.service.ChallengeThemeService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "챌린지 테마 Controller", description = "챌린지 테마 관리 API")
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
@RequestMapping("/challenge/theme")
@RequiredArgsConstructor
public class ChallengeThemeController {

    private final ChallengeThemeService challengeThemeService;


    @Operation(summary = "챌린지 테마 조회", description = "챌린지 테마 조회")
    @GetMapping("/theme")
    public ResponseEntity<List<ChallengeThemeResponse>> getChallengeTheme() {
        return ResponseEntity.ok(challengeThemeService.getTheme());
    }

    @Operation(summary = "챌린지 테마 등록", description = "챌린지 테마 등록")
    @PostMapping("/theme")
    public ResponseEntity<ChallengeThemeResponse> createChallengeTheme(@RequestBody ChallengeThemeCreateRequest request) {
        return ResponseEntity.ok(challengeThemeService.createTheme(request));
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
            ))
    })
    @DeleteMapping("/theme/{id}")
    public ResponseEntity<ChallengeUpdateResponse> deleteChallengeTheme(@PathVariable(value = "id") Long id) throws BadRequestException {
        challengeThemeService.deleteChallengeTheme(id);
        return ResponseEntity.ok(new ChallengeUpdateResponse(id, true));
    }
}
