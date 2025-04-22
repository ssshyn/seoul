package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.comment.CommentCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentUpdateRequest;
import com.sm.seoulmate.domain.challenge.service.CommentService;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.Operation;
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
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 조회", description = "댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Page<CommentResponse>> comment(@ParameterObject Pageable pageable,
                                                         @RequestParam LanguageCode languageCode,
                                                         @PathVariable("id") Long id) throws BadRequestException {
        return ResponseEntity.ok(commentService.comment(id, languageCode, pageable));
    }

    @Operation(summary = "내 댓글 조회", description = "내 댓글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @GetMapping("/my")
    public ResponseEntity<Page<CommentResponse>> my(@ParameterObject Pageable pageable,
                                                    @RequestParam LanguageCode languageCode) {
        return ResponseEntity.ok(commentService.my(pageable, languageCode));
    }

    @Operation(summary = "댓글 등록", description = "댓글 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentCreateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.create(request));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @PutMapping
    public ResponseEntity<CommentResponse> update(@RequestBody CommentUpdateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.update(request));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 조회"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "로그인 정보 없음"),
            @ApiResponse(responseCode = "500", description = "시스템 에러")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws BadRequestException {
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
