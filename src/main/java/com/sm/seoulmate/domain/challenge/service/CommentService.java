package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.domain.challenge.dto.CommentCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.CommentResponse;
import com.sm.seoulmate.domain.challenge.dto.CommentUpdateRequest;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.challenge.mapper.CommentMapper;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.CommentRepository;
import com.sm.seoulmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ChallengeRepository challengeRepository;

    /**
     * 댓글 등록
     */
    public CommentResponse create(CommentCreateRequest request) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(request.challengeId()).orElseThrow(() -> new BadRequestException("챌린지 id를 확인해 주세요."));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment comment = CommentMapper.toEntity(StringUtils.trimToEmpty(request.comment()), challenge, user);

        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    /**
     * 댓글 수정
     */
    public CommentResponse update(CommentUpdateRequest request) throws BadRequestException {
        Comment comment = commentRepository.findById(request.commentId()).orElseThrow(() -> new BadRequestException("댓글 id를 확인해 주세요."));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!Objects.equals(comment.getUser().getUserId(), user.getUserId())) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        comment.setComment(StringUtils.trimToEmpty(request.comment()));
        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long id) throws BadRequestException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new BadRequestException("댓글 id를 확인해 주세요."));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!Objects.equals(comment.getUser().getUserId(), user.getUserId())) {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
