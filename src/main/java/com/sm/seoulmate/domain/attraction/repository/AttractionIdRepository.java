package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttractionIdRepository extends JpaRepository<AttractionId, Long> {
    Page<AttractionId> findAll(Pageable pageable);
    Page<AttractionId> findDistinctByAttractionInfos_NameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<AttractionId> findBy(String keyword, Pageable pageable);
}
