package com.sm.seoulmate.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.challenge.enumeration.DisplayRank;
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
@Table(name = "challenge")
public class Challenge {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String nameEng;

    @Column(nullable = false)
    private String titleEng;

    @Column
    private String description;

    @Column
    private String descriptionEng;

    @Column
    private String mainLocation;

    @Column
    private String mainLocationEng;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DisplayRank displayRank;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @JsonBackReference
    private ChallengeTheme challengeTheme;

    @ManyToMany
    @JoinTable(
            name = "challenge_attraction",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "attraction_id")
    )
    private List<AttractionId> attractionIds = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeStatus> statuses = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeLikes> likes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "culture_id", nullable = true) // FK, nullable 허용
    private CulturalEvent culturalEvent;
}
