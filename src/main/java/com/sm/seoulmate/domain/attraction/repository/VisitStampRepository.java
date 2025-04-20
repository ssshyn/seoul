package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitStampRepository extends JpaRepository<VisitStamp, Long> {
    Optional<VisitStamp> findByUserAndAttraction(User user, AttractionId attraction);
}
