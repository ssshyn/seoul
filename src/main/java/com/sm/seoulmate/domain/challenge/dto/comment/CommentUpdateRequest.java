package com.sm.seoulmate.domain.challenge.dto.comment;

import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentUpdateRequest(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "댓글 내용", example = "너무 좋아요~")
        String comment,
        @Schema(description = "언어 코드", example = "KOR")
        LanguageCode languageCode
) {
}
