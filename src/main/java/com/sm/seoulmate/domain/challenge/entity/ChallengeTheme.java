package com.sm.seoulmate.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "challenge_theme")
public class ChallengeTheme {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nameKor;

    @Column
    private String nameEng;

    @Column(nullable = false)
    private String descriptionKor;

    @Column
    private String descriptionEng;

    @OneToMany(mappedBy = "challengeTheme", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Challenge> challenges = new ArrayList<>();
}
