package com.sm.seoulmate.domain.attraction.service;

import com.sm.seoulmate.domain.attraction.feign.FeignInterface;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.enumeration.AttractionCode;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import com.sm.seoulmate.domain.attraction.feign.dto.AttractionsItem;
import com.sm.seoulmate.domain.attraction.feign.dto.SeoulDataResponse;
import com.sm.seoulmate.domain.attraction.feign.dto.ViewNightSpot;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;
    private final FeignInterface feignInterface;

    public boolean setAttractionData() throws Exception {
        setNightViewApiData();
        setTourInfoApiData();

        return true;
    }
    public void setNightViewApiData() {
        SeoulDataResponse seoulDataResponse = feignInterface.getNightView(1, 1000);
        List<ViewNightSpot.NightSpot> viewNightSpots = seoulDataResponse.getViewNightSpot().getRow();
        viewNightSpots.forEach(
                nightSpot -> {
                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(nightSpot.getNum().toString())
                            .attractionCode(AttractionCode.NIGHT)
                            .likes(0L)
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        AttractionInfo attractionInfoEntity = AttractionInfo.builder()
                                .attractionId(attractionIdEntity)
                                .name(StringUtils.trimToEmpty(nightSpot.getTitle()))
                                .description(StringUtils.trimToEmpty(nightSpot.getContents()))
                                .address(StringUtils.trimToEmpty(nightSpot.getAddress()))
                                .homepageUrl(StringUtils.trimToEmpty(nightSpot.getHomageUrl()))
                                .tel(StringUtils.trimToEmpty(nightSpot.getTelNo()))
                                .subway(StringUtils.trimToEmpty(nightSpot.getSubwayInfo()))
                                .freeYn(StringUtils.trimToEmpty(nightSpot.getFreeYn()))
                                .build();

                        attractionInfoRepository.save(attractionInfoEntity);
                    }
                }
        );
    }

    public void setTourInfoApiData() throws Exception {
        // 관광명소 데이터 가져오기
        List<AttractionsItem> attractionsItems = new ArrayList<>();
        Integer lastIndex = 0;
        Integer startIndex = 1;
        Integer endIndex = 1000;

        try {
            do {
                SeoulDataResponse seoulDataResponse = feignInterface.attraction(startIndex, endIndex);
                lastIndex = seoulDataResponse.getTbVwAttractions().getListTotalCount() + 1000;

                attractionsItems.addAll(
                        seoulDataResponse.getTbVwAttractions().getRow().stream()
                                .filter(x -> StringUtils.equals(x.getLangCodeId(), "ko")).toList()
                );

                startIndex += 1000;
                endIndex += 1000;
            } while (lastIndex > endIndex);
        } catch (Exception e) {
            throw new Exception("데이터 조회 중 오류 발생, " + e.getMessage());
        }

        try {
            attractionsItems.forEach(
                    item -> {
                        // 태그 분류
                        List<String> tagList = List.of(item.getTag().split(","));

                        AttractionCode attractionCode = Arrays.stream(AttractionCode.values()).filter(
                                attraction ->
                                        attraction.getFirstKeyword().stream()
                                                .anyMatch(keyword -> tagList.stream().anyMatch(tag -> tag.matches(keyword)))
                        ).findFirst().orElse(null);

                        if (!Objects.isNull(attractionCode)) {

                            AttractionId attractionIdEntity = AttractionId.builder()
                                    .originKey(item.getPostSn())
                                    .attractionCode(attractionCode)
                                    .likes(0L)
                                    .build();

                            Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                            if (!Objects.isNull(attractionId)) {
                                AttractionInfo attractionInfoEntity = AttractionInfo.builder()
                                        .attractionId(attractionIdEntity)
                                        .name(StringUtils.trimToEmpty(item.getPostSj()))
                                        .address(StringUtils.trimToEmpty(item.getAddress()))
                                        .homepageUrl(StringUtils.trimToEmpty(item.getCmmnHmpgUrl()))
                                        .tel(StringUtils.trimToEmpty(item.getCmmnTelNo()))
                                        .subway(StringUtils.trimToEmpty(item.getSubwayInfo()))
                                        .build();

                                attractionInfoRepository.save(attractionInfoEntity);
                            }
                        }
                    }
            );
        } catch (Exception e) {
            throw new Exception("데이터 저장 중 오류 발생, " + e.getMessage());
        }
    }

    public List<AttractionId> getAttractions() {
        return attractionIdRepository.findAll();
    }
}
