package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentResponse;
import com.sm.seoulmate.domain.challenge.dto.comment.CommentUpdateRequest;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.challenge.mapper.CommentMapper;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.CommentRepository;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 목록 조회
     */
    public List<CommentResponse> comment(Long challengeId, LanguageCode languageCode) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));
        List<Comment> commentList = commentRepository.findByChallenge(challenge);
        return commentList.stream().map(comment -> CommentMapper.toResponse(comment, languageCode))
                .sorted(Comparator.comparing(CommentResponse::createdAt).reversed())
                .toList();
    }

    /**
     * 내 댓글 목록 조회
     */
    public List<CommentResponse> my(LanguageCode languageCode) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        List<Comment> commentPage = commentRepository.findByUser(user);
        return commentPage.stream().map(comment -> CommentMapper.toResponse(comment, languageCode)).sorted(Comparator.comparing(CommentResponse::createdAt).reversed()).toList();
    }

    /**
     * 댓글 등록
     */
    public CommentResponse create(CommentCreateRequest request) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(request.challengeId()).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        if(user.getChallengeStatuses().stream().noneMatch(x -> Objects.equals(x.getChallenge(), challenge))) {
            throw new ErrorException(ErrorCode.PERMISSION_DENIED);
        }

        Comment comment = CommentMapper.toEntity(StringUtils.trimToEmpty(request.comment()), challenge, user);

        return CommentMapper.toResponse(commentRepository.save(comment), request.languageCode());
    }

    /**
     * 댓글 수정
     */
    public CommentResponse update(CommentUpdateRequest request) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(request.commentId()).orElseThrow(() -> new ErrorException(ErrorCode.COMMENT_NOT_FOUND));

        if(!Objects.equals(comment.getUser(), user)) {
            throw new ErrorException(ErrorCode.PERMISSION_DENIED);
        }

        comment.setComment(StringUtils.trimToEmpty(request.comment()));
        return CommentMapper.toResponse(commentRepository.save(comment), request.languageCode());
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long id) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.COMMENT_NOT_FOUND));

        if(!Objects.equals(comment.getUser(), user)) {
            throw new ErrorException(ErrorCode.PERMISSION_DENIED);
        }

        commentRepository.delete(comment);
    }
}
