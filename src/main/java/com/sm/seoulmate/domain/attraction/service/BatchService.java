package com.sm.seoulmate.domain.attraction.service;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.enumeration.AttractionCode;
import com.sm.seoulmate.domain.attraction.enumeration.LanguageCode;
import com.sm.seoulmate.domain.attraction.enumeration.SeoulApiCode;
import com.sm.seoulmate.domain.attraction.feign.FeignInterface;
import com.sm.seoulmate.domain.attraction.feign.dto.NightViewFeignResponse;
import com.sm.seoulmate.domain.attraction.feign.dto.SeoulDataResponse;
import com.sm.seoulmate.domain.attraction.feign.dto.TourSpotFeignResponse;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.*;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;
    private final FeignInterface feignInterface;

    public void setAttractionData() throws Exception {
        setNightViewApiData();
        setTourInfoApiData();

    }

    /**
     * 야경명소
     */
    public void setNightViewApiData() {
        SeoulDataResponse<NightViewFeignResponse> seoulDataResponse = (SeoulDataResponse<NightViewFeignResponse>) feignInterface.getSeoulData(SeoulApiCode.NIGHT_VIEW.getApiCode(), 1, 1000);
        List<NightViewFeignResponse> viewNightSpots = seoulDataResponse.getNightViewFeignResponse().getRow();
        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();
        viewNightSpots.forEach(
                nightSpot -> {
                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(nightSpot.getNum().toString())
                            .attractionCode(AttractionCode.NIGHT)
                            .likes(0L)
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        String postalCode = null;
                        String address = StringUtils.trimToEmpty(nightSpot.getAddress());

                        // 우편번호 정규식 패턴 (XXX-XXX 형식)
                        String regex = "\\d{3}-\\d{3}";

                        // 정규식 패턴 컴파일
                        Pattern pattern = Pattern.compile(regex);

                        // 데이터를 매칭할 매처 객체 생성
                        Matcher matcher = pattern.matcher(address);

                        // 매칭되는 우편번호가 있다면 추출
                        if (matcher.find()) {
                            postalCode = matcher.group();  // 우편번호 추출
                            address = address.replace(postalCode, "").trim();  // 우편번호 제외한 나머지 주소
                        }

                        attractionInfoEntities.add(AttractionInfo.builder()
                                .languageCode(LanguageCode.KOR)
                                .attractionId(attractionIdEntity)
                                .name(StringUtils.trimToEmpty(nightSpot.getTitle()))
                                .description(StringUtils.trimToEmpty(nightSpot.getContents()))
                                .postNumber(postalCode)
                                .address(address)
                                .homepageUrl(StringUtils.trimToEmpty(nightSpot.getHomageUrl()))
                                .tel(StringUtils.trimToEmpty(nightSpot.getTelNo()))
                                .subway(StringUtils.trimToEmpty(nightSpot.getSubwayInfo()))
                                .freeYn(StringUtils.trimToEmpty(nightSpot.getFreeYn()))
                                .build());
                    }
                }
        );
        attractionInfoRepository.saveAll(attractionInfoEntities);
    }

    /**
     * 관광명소
     */
    public void setTourInfoApiData() throws Exception {
        // 관광명소 데이터 가져오기
        List<TourSpotFeignResponse> attractionsItems = new ArrayList<>();
        Integer lastIndex = 0;
        Integer startIndex = 1;
        Integer endIndex = 1000;

        try {
            do {
                SeoulDataResponse<TourSpotFeignResponse> seoulDataResponse = (SeoulDataResponse<TourSpotFeignResponse>) feignInterface.getSeoulData(SeoulApiCode.TOUR_SPOT.getApiCode(), startIndex, endIndex);
                lastIndex = seoulDataResponse.getTourSpotFeignResponse().getListTotalCount() + 1000;

                attractionsItems.addAll(
                        seoulDataResponse.getTourSpotFeignResponse().getRow().stream()
                                .filter(x -> StringUtils.equals(x.getLangCodeId(), "ko")).toList()
                );

                startIndex += 1000;
                endIndex += 1000;
            } while (lastIndex > endIndex);
        } catch (Exception e) {
            throw new Exception("데이터 조회 중 오류 발생, " + e.getMessage());
        }

        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();

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
                                String postalCode = null;
                                String address = StringUtils.trimToEmpty(item.getAddress());

                                // 우편번호 정규식 패턴 (XXX-XXX 형식)
                                String regex = "\\d{3}-\\d{3}";

                                // 정규식 패턴 컴파일
                                Pattern pattern = Pattern.compile(regex);

                                // 데이터를 매칭할 매처 객체 생성
                                Matcher matcher = pattern.matcher(address);

                                // 매칭되는 우편번호가 있다면 추출
                                if (matcher.find()) {
                                    postalCode = matcher.group();  // 우편번호 추출
                                    address = address.replace(postalCode, "").trim();  // 우편번호 제외한 나머지 주소
                                }

                                attractionInfoEntities.add(AttractionInfo.builder()
                                        .languageCode(LanguageCode.KOR)
                                        .attractionId(attractionIdEntity)
                                        .name(StringUtils.trimToEmpty(item.getPostSj()))
                                        .postNumber(postalCode)
                                        .address(address)
                                        .homepageUrl(StringUtils.trimToEmpty(item.getCmmnHmpgUrl()))
                                        .tel(StringUtils.trimToEmpty(item.getCmmnTelNo()))
                                        .subway(StringUtils.trimToEmpty(item.getSubwayInfo()))
                                        .build());
                            }
                        }
                    }
            );
            attractionInfoRepository.saveAll(attractionInfoEntities);
        } catch (Exception e) {
            throw new Exception("데이터 저장 중 오류 발생, " + e.getMessage());
        }
    }

    public List<AttractionId> getAttractions() {
        return attractionIdRepository.findAll();
    }
}
