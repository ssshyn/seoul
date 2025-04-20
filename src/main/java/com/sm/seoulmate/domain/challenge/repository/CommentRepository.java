package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByChallenge(Challenge challenge, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
}
