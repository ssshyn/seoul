package com.sm.seoulmate.domain.challenge.entity;

import com.sm.seoulmate.domain.challenge.enumeration.CulturePeriod;
import com.sm.seoulmate.domain.challenge.enumeration.CultureTheme;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "cultural_event")
public class CulturalEvent {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private CultureTheme cultureTheme;

    @Enumerated(EnumType.STRING)
    private CulturePeriod culturePeriod;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @OneToOne(mappedBy = "culturalEvent")
    private Challenge challenge;
}
