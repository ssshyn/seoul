package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.ChallengeUpdateResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentUpdateRequest;
import com.sm.seoulmate.domain.challenge.service.CommentService;
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

@Tag(name = "댓글 Controller", description = "댓글 관리 API")
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
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 조회", description = "댓글 목록 조회")
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
    @GetMapping("/{id}")
    public ResponseEntity<Page<CommentResponse>> comment(@ParameterObject Pageable pageable,
                                                         @RequestParam LanguageCode languageCode,
                                                         @PathVariable("id") Long id) throws BadRequestException {
        return ResponseEntity.ok(commentService.comment(id, languageCode, pageable));
    }

    @Operation(summary = "내 댓글 조회", description = "내 댓글 조회")
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
    public ResponseEntity<Page<CommentResponse>> my(@ParameterObject Pageable pageable,
                                                    @RequestParam LanguageCode languageCode) {
        return ResponseEntity.ok(commentService.my(pageable, languageCode));
    }

    @Operation(summary = "댓글 등록", description = "댓글 등록")
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
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentCreateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.create(request));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0007", description = "댓글 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0007", "message": "댓글 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "U0003", description = "수정 권한이 없습니다.",
                                    value = """
                                            {"code": "U0003", "message": "수정 권한이 없습니다."}
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
    @PutMapping
    public ResponseEntity<CommentResponse> update(@RequestBody CommentUpdateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.update(request));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "R0007", description = "댓글 정보를 조회할 수 없습니다. 다시 확인해 주세요.",
                                    value = """
                                            {"code": "R0007", "message": "댓글 정보를 조회할 수 없습니다. 다시 확인해 주세요."}
                                            """),
                            @ExampleObject(name = "U0003", description = "수정 권한이 없습니다.",
                                    value = """
                                            {"code": "U0003", "message": "수정 권한이 없습니다."}
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ChallengeUpdateResponse> delete(@PathVariable(value = "id") Long id) throws BadRequestException {
        commentService.delete(id);
        return ResponseEntity.ok(new ChallengeUpdateResponse(id, true));
    }
}
