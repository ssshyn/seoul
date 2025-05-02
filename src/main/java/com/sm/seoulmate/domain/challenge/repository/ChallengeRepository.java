package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByChallengeThemeId(Long themeId);
    @Query("SELECT c FROM Challenge c LEFT JOIN c.statuses s GROUP BY c.id ORDER BY COUNT(s) DESC")
    List<Challenge> findAllOrderByStatusCountDesc();
    @Query("SELECT DISTINCT c FROM Challenge c JOIN c.attractionIds a WHERE a IN :attractionIds")
    List<Challenge> findByAttractionIdsIn(@Param("attractionIds") List<AttractionId> attractionIds);

    // 참여자 있는 데이터 수
    @Query("SELECT COUNT(DISTINCT c) FROM Challenge c JOIN c.statuses s")
    long countByStatusesExists();

}
