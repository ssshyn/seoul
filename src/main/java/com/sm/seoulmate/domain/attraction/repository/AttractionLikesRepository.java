package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttractionLikesRepository extends JpaRepository<AttractionLikes, Long> {
    Page<AttractionLikes> findByUser(User user, Pageable pageable);
    Optional<AttractionLikes> findByUserAndAttraction(User user, AttractionId attraction);

}
