package com.sm.seoulmate.domain.attraction.repository;

import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionInfoRepository extends JpaRepository<AttractionInfo, Long> {
    List<AttractionInfo> findByAttractionId_Id(Long id);

    List<AttractionInfo> findByDescriptionIsNull();

    Page<AttractionInfo> findByNameContainingIgnoreCase(String keyword, Pageable pageable);


    //    @Query(value = """
//    SELECT ai.attraction_id AS attractionId,
//        CAST(ai.locationx AS DOUBLE) AS locationX,
//        CAST(ai.locationy AS DOUBLE) AS locationY,
//        (
//            6371000 * acos(
//                cos(radians(:locationY)) * cos(radians(ai.locationy)) *
//                cos(radians(ai.locationx) - radians(:locationX)) +
//                sin(radians(:locationY)) * sin(radians(ai.locationy))
//            )
//        ) AS distance
//    FROM attraction_info ai
//    HAVING distance < :radius
//    ORDER BY distance
//    LIMIT :limit
//    """, nativeQuery = true)
    @Query(value = """
                SELECT *
                                FROM (
                                        SELECT ai.attraction_id AS attractionId,
                                        CAST(ai.locationx AS DOUBLE) AS locationX,
                                CAST(ai.locationy AS DOUBLE) AS locationY,
                                       (
                                               6371000 * acos(
                                        cos(radians(:locationY)) * cos(radians(ai.locationy)) *
                                cos(radians(ai.locationx) - radians(:locationX)) +
                                sin(radians(:locationY)) * sin(radians(ai.locationy))
                                        )
                                        ) AS distance
                                FROM attraction_info ai
                            ) AS distances
                                WHERE distance < :radius
                                ORDER BY distance
                                LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findNearbyAttraction(
            @Param("locationX") Double locationX,
            @Param("locationY") Double locationY,
            @Param("radius") Integer radius,
            @Param("limit") Integer limit
    );

    @Query("""
                SELECT a FROM AttractionInfo a 
                WHERE a.attractionId.id IN (
                    SELECT ai.attractionId.id FROM AttractionInfo ai
                    GROUP BY ai.attractionId.id
                    HAVING SUM(CASE WHEN ai.languageCode = :lang THEN 1 ELSE 0 END) = 0
                )
            """)
    List<AttractionInfo> findAllByAttractionIdGroupWithoutLanguage(@Param("lang") LanguageCode lang);
}
