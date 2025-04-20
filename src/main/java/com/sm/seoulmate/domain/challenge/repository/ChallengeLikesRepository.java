package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeLikes;
import com.sm.seoulmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeLikesRepository extends JpaRepository<ChallengeLikes, Long> {
    Optional<ChallengeLikes> findByUserAndChallenge(User user, Challenge challenge);
}
