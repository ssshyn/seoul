package com.sm.seoulmate.domain.attraction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sm.seoulmate.domain.attraction.enumeration.LanguageCode;
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
@Table(name = "attraction_info")
public class AttractionInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;

    @Column
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String postNumber;

    @Column
    private String address;

    @Column
    private String locationX;

    @Column
    private String locationY;

    @Column
    private String operDay;

    @Column
    private String operOpenTime;

    @Column
    private String operCloseTime;

    @Column
    private String homepageUrl;

    @Column
    private String tel;

    @Column
    private String subway;

    @Column
    private String freeYn;

    @ManyToOne
    @JoinColumn(name = "attraction_id")
    @JsonBackReference
    private AttractionId attractionId;
}
