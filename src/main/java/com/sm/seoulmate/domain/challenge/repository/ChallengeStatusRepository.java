package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeStatus;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeStatusRepository extends JpaRepository<ChallengeStatus, Long> {
    List<ChallengeStatus> findByUserAndChallengeStatusCode(User user, ChallengeStatusCode challengeStatusCode);
    Optional<ChallengeStatus> findByUserAndChallenge(User user, Challenge challenge);
}
