package com.sm.seoulmate.domain.challenge.entity;

import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "challenge_status")
public class ChallengeStatus {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeStatusCode challengeStatusCode;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime modifiedAt;
}
