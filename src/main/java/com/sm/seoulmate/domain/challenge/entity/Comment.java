package com.sm.seoulmate.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sm.seoulmate.domain.login.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    @JsonBackReference
    private Challenge challenge;

    // 연관된 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
