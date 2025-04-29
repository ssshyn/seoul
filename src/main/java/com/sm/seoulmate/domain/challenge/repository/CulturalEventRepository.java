package com.sm.seoulmate.domain.challenge.repository;

import com.sm.seoulmate.domain.challenge.entity.CulturalEvent;
import com.sm.seoulmate.domain.challenge.enumeration.CulturePeriod;
import com.sm.seoulmate.domain.challenge.enumeration.CultureTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long> {
    List<CulturalEvent> findByCultureTheme(CultureTheme cultureTheme);
    List<CulturalEvent> findByCulturePeriodNot(CulturePeriod period);
}
