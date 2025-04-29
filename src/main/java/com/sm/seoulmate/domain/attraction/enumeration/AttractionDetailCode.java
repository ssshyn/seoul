package com.sm.seoulmate.domain.attraction.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AttractionDetailCode {
    MUST_VISIT(AttractionThemeCode.MUSTVISIT, "필수 관광 명소", "Must-Visit", new ArrayList<>(), Arrays.asList("63스퀘어", "아쿠아플라넷63", "국회의사당")),
    OLD_PALACE(AttractionThemeCode.KCULTURE, "고궁", "Old Palace", Arrays.asList("고궁", ".*궁궐.*", ".*고궁투어.*"), Arrays.asList(".*경복궁.*")),
    HANOK(AttractionThemeCode.KCULTURE, "한옥", "HANOK", new ArrayList<>(), Arrays.asList(".*한옥마을.*", ".*가옥.*")),
    OLD_STREET(AttractionThemeCode.KCULTURE, "전통거리", "Old Street", Arrays.asList(".*문화거리.*"), Arrays.asList(".*거리.*", "돈의문박물관마을", "순라길")),
    HISTORIC_SITE(AttractionThemeCode.KCULTURE, "역사 유적지", "Historic Site", Arrays.asList(".*유적.*", ".*역사갤러리.*", ".*서울역사.*", ".*사대문.*", ".*성문.*", ".*4소문.*", ".*유네스코.*", ".*부분개방부지.*", ".*대한민국역사.*", "환구단"), Arrays.asList("광화문광장")),
    TRADITIONAL(AttractionThemeCode.TRADITIONAL, "전통 체험", "Traditional Culture Experience", Arrays.asList(".*전통체험.*"), Arrays.asList("영학정")),
    NIGHTVIEW(AttractionThemeCode.NIGHTVIEW, "야경 & 전망", "Night View", Arrays.asList(".*전망.*, .*야경.*"), new ArrayList<>()),
    FOOD(AttractionThemeCode.OLDPLACE, "음식점", "Restaurant", Arrays.asList("냉면", "분식", "중식", ".*음식점.*", ".*미쉐린.*", ".*한식.*", ".*주점.*", ".*호프.*", ".*수제비.*", ".*국밥.*", ".*닭ㅍ", ".*아귀찜ㅍ", ".*국수.*", ".*식당"), new ArrayList<>()),
    CAFE(AttractionThemeCode.OLDPLACE, "카페", "Coffee SHOP", Arrays.asList(".*커피.*", ".*다방.*", ".*찻집.*"), new ArrayList<>()),
    RICECAKE(AttractionThemeCode.OLDPLACE, "떡집", "Rice Cake Shop", Arrays.asList(".*떡집.*"), Arrays.asList(".*떡집.*")),
    BAKERY(AttractionThemeCode.OLDPLACE, "베이커리", "Bakery", Arrays.asList("빵"), new ArrayList<>()),
    OLD_ETC(AttractionThemeCode.OLDPLACE, "오래가게", "Old Place", Arrays.asList(), new ArrayList<>()),
    PHOTO_SPOT(AttractionThemeCode.EMOTION, "SNS 포토 스팟", "SNS Photo Spot", Arrays.asList("ECC", ".*단풍명소.*"), new ArrayList<>()),
    K_DRAMA(AttractionThemeCode.EMOTION, "K-촬영지", "K-Drama Studio", Arrays.asList(".*촬영지.*", "런닝맨", "겨울연가"), new ArrayList<>()),
    ALLEYWAY(AttractionThemeCode.EMOTION, "골목", "Alleyway", Arrays.asList(".*골목.*"), Arrays.asList("순라길", "창신동 절벽마을")),
    WALK(AttractionThemeCode.EMOTION, "산책길", "Walking Path", Arrays.asList(".*단풍길.*", ".*산책로.*"), Arrays.asList(".*돌담길.*", "순라길", "초대길")),
    PARK(AttractionThemeCode.HEALING, "공원", "Park", new ArrayList<>(), Arrays.asList(".*공원.*", "창덕궁 후원")),
    HAN_RIVER(AttractionThemeCode.HEALING, "한강", "HAN River", Arrays.asList(".*대교.*"), new ArrayList<>()),
    NAMSAN(AttractionThemeCode.HEALING, "남산", "NAMSAN", Arrays.asList(".*남산.*"), new ArrayList<>()),
    TEMPLE(AttractionThemeCode.RELIGION, "사찰", "Temple", Arrays.asList("절", ".*사찰.*", ".*템플스테이.*"), new ArrayList<>()),
    CATHEDRAL(AttractionThemeCode.RELIGION, "성당", "Cathedral", Arrays.asList("성당", "천주교"), new ArrayList<>()),
    CHURCH(AttractionThemeCode.RELIGION, "교회", "CHURCH", new ArrayList<>(), Arrays.asList(".*교회.*")),
    ART_MUSEUM(AttractionThemeCode.MUSIUM, "미술관&박물관", "Art Galleries and Museums", Arrays.asList("갤러리", "박물관", "봉제역사관", ".*미술관.*", ".*과학관.*", ".*미술전시.*", ".*서울도서관.*"), Arrays.asList(".*박물관.*", ".*갤러리ㅍ", ".*전시관.*", ".*뮤지엄.*", ".*기념관.*", ".*기념도서관.*", ".*서울도서관.*", ".*공공도서관.*", "국립중앙도서관", "서울기록원", "서울기록문화관")),
    COMPLEX_CULTURE(AttractionThemeCode.MUSIUM, "복합문화공간", "Complex Cultural Space", Arrays.asList("ECC", "SJ쿤스트할레", "DMC", ".*컨벤션.*", ".*복합문화공간.*", "서울도시재생이야기관", "뮤직라이브러리"), Arrays.asList("명동재미로", "손기정문화도서관", "별마당 도서관", "서울광장", "서울상상나라", "서울중앙우체국", "세운상가", "정동1928 아트센터", "정동극장", "청와대", "광화문광장", "안녕인사동")),
    ETC(AttractionThemeCode.ETC, "기타", "ETC", new ArrayList<>(), new ArrayList<>()),
    MARKET(AttractionThemeCode.OLDPLACE, "전통시장", "Traditional Market", new ArrayList<>(), new ArrayList<>()),
    CULTURE(AttractionThemeCode.CULTURE, "문화행사", "Cultural Event", new ArrayList<>(), new ArrayList<>());

    private final AttractionThemeCode themeCode;
    private final String descriptionKor;
    private final String descriptionEng;
    private final List<String> tagValues;
    private final List<String> nameValues;

    public static List<AttractionDetailCode> getOldShop() {
        return Arrays.asList(FOOD, CAFE, RICECAKE, BAKERY);
    }
}


