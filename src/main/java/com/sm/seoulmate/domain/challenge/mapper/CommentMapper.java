package com.sm.seoulmate.domain.challenge.mapper;

import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.util.UserInfoUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CommentMapper {
    public static Comment toEntity(String comment, Challenge challenge, User user) {
        return Comment.builder()
                .comment(StringUtils.trimToEmpty(comment))
                .challenge(challenge)
                .user(user)
                .build();
    }

    public static CommentResponse toResponse(Comment comment, LanguageCode languageCode) {
        String userId = UserInfoUtil.getUserId();
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                Objects.equals(languageCode, LanguageCode.KOR) ? comment.getUser().getNicknameKor() : comment.getUser().getNicknameEng(),
                StringUtils.equals(userId, comment.getUser().getUserId()),
                comment.getChallenge().getId(),
                comment.getCreatedAt()
        );
    }
}
