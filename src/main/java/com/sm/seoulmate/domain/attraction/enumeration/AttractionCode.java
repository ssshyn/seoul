package com.sm.seoulmate.domain.attraction.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum AttractionCode {
    KFOOD("한식 투어", List.of(".*떡", ".*밥", ".*찜", ".*분식", ".*식당")),
    DESSERT("디저트 투어", List.of(".*빵", ".*브런치.*", ".*케이크.*")),
    COFFEE("카페 & 커피", List.of(".*커피", ".*카페")),
    HEALING("자연 속 힐링", List.of(".*길", ".*식물원")),
    MOOD("분위기 명소", List.of(".*명소", "낭만.*", "감성.*")),
    WALK("산책", List.of("걷기", ".*길", ".*산책")),
    TREND("트렌디", List.of("핫플.*", ".*만한곳")),
    DATE("데이트 명소", List.of(".*데이트", ".*코스", ".*명소")),
    ART("전시 & 미술", List.of(".*예술", ".*전시", ".*미술", ".*아트")),
    BOOK("북카페 & 공방", List.of(".*서점", ".*공방", "디자인", ".*공간")),
    HISTORY("역사 & 문화", List.of(".*한옥.*", ".*박물관", "문화.*")),
    TRADITION("전통 체험", List.of("전통.*", ".*민속.*", ".*공예")),
    NIGHT("야경 명소", List.of("야경.*", "밤.*")),
    NIGHTLIFE("나이트 라이프", List.of(".*포차", ".*바", "루프탑"));

    final String description;
    final List<String> firstKeyword;

}
