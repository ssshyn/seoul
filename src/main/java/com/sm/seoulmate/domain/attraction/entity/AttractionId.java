package com.sm.seoulmate.domain.attraction.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sm.seoulmate.domain.attraction.enumeration.AttractionCode;
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

    @Column
    private String originKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttractionCode attractionCode;

    @Column(nullable = false)
    private Long likes;

    @OneToMany(mappedBy = "attractionId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AttractionInfo> attractionInfos = new ArrayList<>();
}
