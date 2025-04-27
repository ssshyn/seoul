package com.sm.seoulmate.domain.challenge.dto.comment;

import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "댓글 내용", example = "너무 좋아요~")
        String comment,
        @Schema(description = "작성자 닉네임", example = "삐뽀 화로")
        String nickname,
        @Schema(description = "본인 작성 여부(로그인)")
        Boolean isMine,
        @Schema(description = "챌린지 ID", example = "1")
        Long challengeId,
        @Schema(description = "작성 일자")
        LocalDateTime createdAt,
        @Schema(description = "유저 진행상태")
        ChallengeStatusCode challengeStatusCode
) {
}
