package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionInfoRepository extends JpaRepository<AttractionInfo, Long> {
    Page<AttractionInfo> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
