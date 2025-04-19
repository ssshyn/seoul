package com.sm.seoulmate.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Column
    private String description;

    @Column
    private Long mainAttractionId;

    @Column
    private String mainBorough;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer level;

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
}
