package com.sm.seoulmate.domain.attraction.service;

import com.sm.seoulmate.domain.attraction.dto.AttractionResponse;
import com.sm.seoulmate.domain.attraction.dto.AttractionSearchCondition;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;

    /**
     * 관광지 목록 조회 - 페이징
     */
    public Page<AttractionResponse> getAttractions(AttractionSearchCondition condition, Pageable pageable) {
        String keyword = StringUtils.trimToEmpty(condition.keyword());
        Page<AttractionId> attractionIdPage = attractionIdRepository.findDistinctByAttractionInfos_NameContainingIgnoreCase(keyword, pageable);
        return attractionIdPage.map(AttractionMapper::toResponse);
    }
}
