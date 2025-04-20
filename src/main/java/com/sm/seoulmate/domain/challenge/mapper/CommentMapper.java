package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.user.entity.User;
import org.apache.commons.lang3.StringUtils;

public class CommentMapper {
    public static Comment toEntity(String comment, Challenge challenge, User user) {
        return Comment.builder()
                .comment(StringUtils.trimToEmpty(comment))
                .challenge(challenge)
                .user(user)
                .build();
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getChallenge().getId(),
                comment.getCreatedAt()
        );
    }
}
