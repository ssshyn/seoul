package com.sm.seoulmate.domain.challenge.service;

import com.google.common.base.Strings;
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
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public Page<CommentResponse> comment(Long challengeId, LanguageCode languageCode, Pageable pageable) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new BadRequestException("챌린지 id를 확인해 주세요."));
        Page<Comment> commentPage = commentRepository.findByChallenge(challenge, pageable);
        return commentPage.map(comment -> CommentMapper.toResponse(comment, languageCode));
    }

    /**
     * 내 댓글 목록 조회
     */
    public Page<CommentResponse> my(Pageable pageable, LanguageCode languageCode) {
        Long userId = UserInfoUtil.getUserId();

        if(Objects.isNull(userId)) {
           return null;
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
        Page<Comment> commentPage = commentRepository.findByUser(user, pageable);
        return commentPage.map(comment -> CommentMapper.toResponse(comment, languageCode));
    }

    /**
     * 댓글 등록
     */
    public CommentResponse create(CommentCreateRequest request) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(request.challengeId()).orElseThrow(() -> new BadRequestException("챌린지 id를 확인해 주세요."));
        User user = userRepository.findById(Objects.requireNonNull(UserInfoUtil.getUserId())).orElseThrow(() -> new UsernameNotFoundException("로그인 정보를 찾을 수 없습니다."));

        Comment comment = CommentMapper.toEntity(StringUtils.trimToEmpty(request.comment()), challenge, user);

        return CommentMapper.toResponse(commentRepository.save(comment), request.languageCode());
    }

    /**
     * 댓글 수정
     */
    public CommentResponse update(CommentUpdateRequest request) throws BadRequestException {
        Comment comment = commentRepository.findById(request.commentId()).orElseThrow(() -> new BadRequestException("댓글 id를 확인해 주세요."));

        if(!Objects.equals(comment.getUser().getId(), UserInfoUtil.getUserId())) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        comment.setComment(StringUtils.trimToEmpty(request.comment()));
        return CommentMapper.toResponse(commentRepository.save(comment), request.languageCode());
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long id) throws BadRequestException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new BadRequestException("댓글 id를 확인해 주세요."));

        if(!Objects.equals(comment.getUser().getId(), UserInfoUtil.getUserId())) {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
