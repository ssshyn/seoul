package com.sm.seoulmate.domain.attraction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sm.seoulmate.domain.attraction.enumeration.AttractionDetailCode;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
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
@Table(name = "attraction_id")
public class AttractionId {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String originKey;

    @ElementCollection
    private List<AttractionDetailCode> attractionDetailCodes;

    @Column(nullable = false)
    private Long likes;

    @OneToMany(mappedBy = "attractionId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AttractionInfo> attractionInfos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @JsonBackReference
    private Challenge challenge_id;
}
