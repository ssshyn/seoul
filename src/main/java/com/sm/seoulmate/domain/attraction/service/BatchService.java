package com.sm.seoulmate.domain.attraction.service;

import com.google.common.base.Strings;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.enumeration.AttractionDetailCode;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.attraction.enumeration.SeoulApiCode;
import com.sm.seoulmate.domain.attraction.feign.FeignInterface;
import com.sm.seoulmate.domain.attraction.feign.NaverFeign;
import com.sm.seoulmate.domain.attraction.feign.NaverFeignInterface;
import com.sm.seoulmate.domain.attraction.feign.TourApiFeign;
import com.sm.seoulmate.domain.attraction.feign.dto.*;
import com.sm.seoulmate.domain.attraction.feign.dto.tourApiDto.KeywordResponse;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;
    private final FeignInterface feignInterface;
    private final NaverFeignInterface naverLocalFeign;
    private final NaverFeign naverMapFeign;
    private final TourApiFeign tourApiFeign;

    @Value("${naver.clientId}")
    private String naverClientId;
    @Value("${naver.clientSecret}")
    private String naverClientSecret;
    @Value("${naver.map.clientId}")
    private String naverMapClientId;
    @Value("${naver.map.clientSecret}")
    private String naverMapClientSecret;


    @Transactional
    public void setAttractionData() throws Exception {
        setTourInfoApiData();
        setNightViewApiData();
        setHanokApiData();
        setTourRoadApiData();
        setMountainParkApiData();
        setCooperation();
        setTourApiInfo();
    }

    /**
     * 산과공원
     */
    public void setMountainParkApiData() {
        SeoulDataResponse<MountainParkFeignResponse> seoulDataResponse = (SeoulDataResponse<MountainParkFeignResponse>) feignInterface.getSeoulData(SeoulApiCode.MOUNTAIN_PARK.getApiCode(), 1, 1000);
        List<MountainParkFeignResponse> mountains = seoulDataResponse.getMountainParkFeignResponse().getRow();
        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();

        mountains.forEach(
                mountain -> {
                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(mountain.getOriginId())
                            .attractionDetailCodes(Collections.singletonList(AttractionDetailCode.PARK))
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        attractionInfoEntities.add(AttractionInfo.builder()
                                .languageCode(LanguageCode.KOR)
                                .attractionId(attractionIdEntity)
                                .name(StringUtils.trimToEmpty(mountain.getName()))
                                .address(StringUtils.trimToEmpty(mountain.getAddress()))
                                .tel(StringUtils.trimToEmpty(mountain.getTel()))
                                .subway(StringUtils.trimToEmpty(mountain.getSubway()))
                                .build());
                    }
                }
        );
        attractionInfoRepository.saveAll(attractionInfoEntities);
    }

    /**
     * 관광거리
     */
    public void setTourRoadApiData() {
        SeoulDataResponse<TourRoadFeignResponse> seoulDataResponse = (SeoulDataResponse<TourRoadFeignResponse>) feignInterface.getSeoulData(SeoulApiCode.TOUR_ROAD.getApiCode(), 1, 1000);
        List<TourRoadFeignResponse> roads = seoulDataResponse.getTourRoadFeignResponse().getRow();
        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();

        roads.forEach(
                road -> {
                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(road.getOriginId())
                            .attractionDetailCodes(Collections.singletonList(AttractionDetailCode.ALLEYWAY))
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        attractionInfoEntities.add(AttractionInfo.builder()
                                .languageCode(LanguageCode.KOR)
                                .attractionId(attractionIdEntity)
                                .name(StringUtils.trimToEmpty(road.getName()))
                                .address(StringUtils.trimToEmpty(road.getAddress()))
                                .locationX(StringUtils.trimToEmpty(road.getLocationX()))
                                .locationY(StringUtils.trimToEmpty(road.getLocationY()))
                                .build());
                    }
                }
        );
        attractionInfoRepository.saveAll(attractionInfoEntities);
    }

    /**
     * 공공한옥
     */
    public void setHanokApiData() {
        SeoulDataResponse<HanokFeignResponse> seoulDataResponse = (SeoulDataResponse<HanokFeignResponse>) feignInterface.getSeoulData(SeoulApiCode.HANOK.getApiCode(), 1, 1000);
        List<HanokFeignResponse> hanoks = seoulDataResponse.getHanokFeignResponse().getRow();
        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();

        hanoks.forEach(
                hanok -> {
                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(hanok.getOriginId().toString())
                            .attractionDetailCodes(Collections.singletonList(AttractionDetailCode.HANOK))
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        attractionInfoEntities.add(AttractionInfo.builder()
                                .languageCode(LanguageCode.KOR)
                                .attractionId(attractionIdEntity)
                                .name(StringUtils.trimToEmpty(hanok.getName()))
                                .description(StringUtils.trimToEmpty(Jsoup.parse(hanok.getDescription()).text()))
                                .address(StringUtils.trimToEmpty(hanok.getAddress()))
                                .locationX(StringUtils.trimToEmpty(hanok.getLocationX()))
                                .locationY(StringUtils.trimToEmpty(hanok.getLocationY()))
                                .homepageUrl(StringUtils.trimToEmpty(hanok.getHomepage()))
                                .tel(StringUtils.trimToEmpty(hanok.getTel()))
                                .subway(StringUtils.trimToEmpty(hanok.getSubway()))
                                .build());
                    }
                }
        );
        attractionInfoRepository.saveAll(attractionInfoEntities);
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
                    // 상호명 분류
                    List<AttractionDetailCode> attractionDetailCodeList = new ArrayList<>();
                    String attractionName = StringUtils.trimToEmpty(nightSpot.getName());

                    attractionDetailCodeList.add(AttractionDetailCode.NIGHTVIEW);   // 고정 데이터
                    if (attractionName.contains("공원")) {
                        attractionDetailCodeList.add(AttractionDetailCode.PARK);
                    }
                    if (attractionName.contains("한강") || attractionName.contains("대교") || attractionName.contains("뚝섬")) {
                        attractionDetailCodeList.add(AttractionDetailCode.HAN_RIVER);
                    }
                    if (attractionName.contains("남산")) {
                        attractionDetailCodeList.add(AttractionDetailCode.NAMSAN);
                    }
                    if (attractionName.contains("길")) {
                        attractionDetailCodeList.add(AttractionDetailCode.WALK);
                    }

                    AttractionId attractionIdEntity = AttractionId.builder()
                            .originKey(nightSpot.getOriginId().toString())
                            .attractionDetailCodes(attractionDetailCodeList)
                            .build();

                    Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                    if (!Objects.isNull(attractionId)) {
                        attractionInfoEntities.add(AttractionInfo.builder()
                                .languageCode(LanguageCode.KOR)
                                .attractionId(attractionIdEntity)
                                .name(attractionName)
                                .description(StringUtils.trimToEmpty(Jsoup.parse(nightSpot.getDescription()).text()))
                                .address(StringUtils.trimToEmpty(nightSpot.getAddress()))
                                .homepageUrl(StringUtils.trimToEmpty(nightSpot.getHomageUrl()))
                                .tel(StringUtils.trimToEmpty(nightSpot.getTel()))
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
                                .filter(x -> StringUtils.equals(x.getLanguageCode(), "ko")).toList()
                );

                startIndex += 1000;
                endIndex += 1000;
            } while (lastIndex > endIndex);
        } catch (Exception e) {
            throw new Exception("데이터 조회 중 오류 발생, " + e.getMessage());
        }

        List<AttractionInfo> attractionInfoEntities = new ArrayList<>();
        List<AttractionDetailCode> detailCodes = List.of(AttractionDetailCode.values());

        try {
            attractionsItems.forEach(
                    item -> {
                        // 해당 관광지에 포함될 분류값 저장할 배열
                        List<AttractionDetailCode> attractionCodes = new ArrayList<>();

                        // 분류할 데이터 수집
                        List<String> tagList = List.of(item.getTag().split(","));     // 태그 목록
                        String attractionName = StringUtils.trimToEmpty(item.getName());  // 관광지명

                        // 1. 태그로 분류를 나눈다.
                        tagList.forEach(tag ->
                                detailCodes.forEach(detailCode ->
                                        detailCode.getTagValues().forEach(
                                                tagValue -> {
                                                    if (StringUtils.trimToEmpty(tag).matches(tagValue)) {
                                                        attractionCodes.add(detailCode);
                                                    }
                                                }
                                        )
                                )

                        );


                        // 2. 상호명 분류
                        detailCodes.forEach(detailCode ->
                                detailCode.getNameValues().forEach(
                                        nameValue -> {
                                            if (attractionName.matches(nameValue)) {
                                                attractionCodes.add(detailCode);
                                            }
                                        }
                                ));

                        // 3. 상호명 예외처리 - 하드코딩
                        if (StringUtils.equals(attractionName, "삼양탕")) {
                            attractionCodes.add(AttractionDetailCode.CAFE);
                        }
                        if (StringUtils.equals(attractionName, "용산공원갤러리")) {
                            attractionCodes.add(AttractionDetailCode.PARK);
                        }
                        if (StringUtils.contains(attractionName, "골목길")) {
                            attractionCodes.add(AttractionDetailCode.ART_MUSEUM);
                        }
                        // 4. 태그 분류 예외처리 - 오래가게 필터링 하드코딩 요소..
                        boolean isOldShop = tagList.contains("오래가게");
                        if (isOldShop &&
                                attractionCodes.stream().noneMatch(code -> AttractionDetailCode.getOldShop().contains(code))) {
                            attractionCodes.add(AttractionDetailCode.OLD_ETC);
                        }
                        // 아무것도 속하지 않으면 기타로 나누기
                        if (attractionCodes.isEmpty()) {
                            attractionCodes.add(AttractionDetailCode.ETC);
                        }

                        // id entity 저장
                        AttractionId attractionIdEntity = AttractionId.builder()
                                .originKey(item.getOriginId())
                                .attractionDetailCodes(attractionCodes.stream().distinct().toList())
                                .build();

                        Long attractionId = attractionIdRepository.save(attractionIdEntity).getId();

                        // 관광 상세 entity 저장
                        if (!Objects.isNull(attractionId)) {
                            attractionInfoEntities.add(AttractionInfo.builder()
                                    .languageCode(LanguageCode.KOR)
                                    .attractionId(attractionIdEntity)
                                    .name(StringUtils.trimToEmpty(item.getName()))
                                    .description(StringUtils.trimToEmpty(item.getDescription()))
                                    .address(StringUtils.trimToEmpty(item.getAddress()).isEmpty()
                                            ? StringUtils.trimToEmpty(item.getNewAddress())
                                            : StringUtils.trimToEmpty(item.getAddress()))
                                    .homepageUrl(StringUtils.trimToEmpty(item.getHompage()))
                                    .tel(StringUtils.trimToEmpty(item.getTel()))
                                    .subway(StringUtils.trimToEmpty(item.getSubway()))
                                    .build());
                        }
                    }
            );
            attractionInfoRepository.saveAll(attractionInfoEntities);
        } catch (
                Exception e) {
            throw new Exception("데이터 저장 중 오류 발생, " + e.getMessage());
        }
    }

    /**
     * 주소정보세팅
     */
    public void setCooperation() {
        List<AttractionInfo> attractionInfoList = attractionInfoRepository.findAll();

        for(AttractionInfo attractionInfo : attractionInfoList) {
            String address = StringUtils.trimToEmpty(attractionInfo.getAddress());
            String locationX = StringUtils.trimToEmpty(attractionInfo.getLocationX());
            String locationY = StringUtils.trimToEmpty(attractionInfo.getLocationY());

            if(!Strings.isNullOrEmpty(address) && !Strings.isNullOrEmpty(locationX) && !Strings.isNullOrEmpty(locationY)) {
                continue;
            }

            // 우편번호 정규식 패턴 (XXX-XXX 형식)
            String regex = "\\d{3}-\\d{3}";

            // 정규식 패턴 컴파일
            Pattern pattern = Pattern.compile(regex);

            // 데이터를 매칭할 매처 객체 생성
            Matcher matcher = pattern.matcher(address);

            // 매칭되는 우편번호가 있다면 추출
            if (matcher.find()) {
                String postalCode = matcher.group();  // 우편번호 추출
                address = address.replace(postalCode, "").trim();  // 우편번호 제외한 나머지 주소
            }

            // 좌표 구하기
            String mapY = "", mapX = "";
            try {
                Thread.sleep(200L);


                if (Strings.isNullOrEmpty(address)) {
                    // 주소 정보가 없으면 네이버 검색 > 지역 검색 api
                    NaverLocalResponse naverLocalResponse = naverLocalFeign.getNaverLocal(naverClientId, naverClientSecret, attractionInfo.getName(), 1);
                    if (!naverLocalResponse.getItems().isEmpty()) {
                        NaverLocalResponse.Item naverLocal = naverLocalResponse.getItems().get(0);
                        address = naverLocal.getAddress();
                        mapX = naverLocal.getMapx().substring(0, 3) + "." + naverLocal.getMapx().substring(3);
                        mapY = naverLocal.getMapy().substring(0, 2) + "." + naverLocal.getMapy().substring(2);
                    }
                } else {
                    // 주소 정보가 있으면 네이버 지도 > 좌표 검색
                    NaverMapResponse naverMapResponse = naverMapFeign.getCoordinate(naverMapClientId, naverMapClientSecret, "application/json", address);
                    if (!naverMapResponse.getAddresses().isEmpty()) {
                        NaverMapResponse.Address naverMapAddress = naverMapResponse.getAddresses().get(0);
                        mapX = naverMapAddress.getX();
                        mapY = naverMapAddress.getY();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }

            attractionInfo.setAddress(address);
            attractionInfo.setLocationX(mapX);
            attractionInfo.setLocationY(mapY);

        }
        attractionInfoRepository.saveAll(attractionInfoList);
    }

    /**
     * 소개글/이미지 정보 세팅(1차)
     * tourApi
     */
    public void setTourApiInfo() {
        List<AttractionInfo> attractionInfoList = attractionInfoRepository.findAll();
        attractionInfoList.forEach(
                attractionInfo -> {
                    String tourTypeId = "";
                    String tourId = "";
                    String attractionDescription = attractionInfo.getDescription();
                    String attractionImage = attractionInfo.getImageUrl();

                    KeywordResponse keywordResponse = tourApiFeign.searchKeyword(attractionInfo.getName());
                    KeywordResponse.KeywordItems tourItems = keywordResponse.getTourKeywordResponse().getKeywordBody().getItems();
                    if (!Objects.isNull(tourItems)) {
                        KeywordResponse.KeywordItem keywordItem = tourItems.getItem().get(0);
                        tourTypeId = keywordItem.getContentTypeId();
                        tourId = keywordItem.getContentId();

                        if(Strings.isNullOrEmpty(attractionInfo.getImageUrl())) {
                            KeywordResponse imageResponse = tourApiFeign.getTourImages(tourId);
                            KeywordResponse.KeywordItems imageItems = imageResponse.getTourKeywordResponse().getKeywordBody().getItems();
                            if (!Objects.isNull(imageItems)) {
                                KeywordResponse.KeywordItem imageItem = imageItems.getItem().get(0);
                                attractionImage = imageItem.getImageUrl();
                            }
                        }

                        if(Strings.isNullOrEmpty(attractionInfo.getDescription())) {
                            KeywordResponse descriptionResponse = tourApiFeign.getDescription(tourId, tourTypeId);
                            KeywordResponse.KeywordItems descriptionItems = descriptionResponse.getTourKeywordResponse().getKeywordBody().getItems();
                            if (!Objects.isNull(descriptionItems)) {
                                KeywordResponse.KeywordItem descriptionItem = descriptionItems.getItem().get(0);
                                attractionDescription = Jsoup.parse(descriptionItem.getDescription()).text();
                            }
                        }

                        attractionInfo.setImageUrl(attractionImage);
                        attractionInfo.setDescription(attractionDescription);
                    }
                }
        );
        attractionInfoRepository.saveAll(attractionInfoList);
    }
}
