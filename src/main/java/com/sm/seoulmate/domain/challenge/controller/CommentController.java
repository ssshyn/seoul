package com.sm.seoulmate.domain.challenge.controller;

import com.sm.seoulmate.domain.challenge.dto.comment.CommentCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentUpdateRequest;
import com.sm.seoulmate.domain.challenge.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글 Controller", description = "댓글 관리 API")
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "댓글 등록")
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentCreateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.create(request));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping
    public ResponseEntity<CommentResponse> update(@RequestBody CommentUpdateRequest request) throws BadRequestException {
        return ResponseEntity.ok(commentService.update(request));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws BadRequestException {
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
