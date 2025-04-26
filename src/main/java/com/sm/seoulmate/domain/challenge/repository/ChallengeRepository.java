package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Challenge> findByChallengeThemeId(Long themeId);
    @Query("SELECT c FROM Challenge c LEFT JOIN c.statuses s GROUP BY c.id ORDER BY COUNT(s) DESC")
    List<Challenge> findAllOrderByStatusCountDesc();
}
