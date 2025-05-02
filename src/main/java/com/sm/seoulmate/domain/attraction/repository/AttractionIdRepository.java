package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttractionIdRepository extends JpaRepository<AttractionId, Long> {
    List<AttractionId> findDistinctByAttractionInfos_NameContainingIgnoreCase(String keyword);
}
