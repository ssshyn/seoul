package com.sm.seoulmate.domain.challenge.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentUpdateRequest(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "댓글 내용", example = "너무 좋아요~")
        String comment
) {
}
