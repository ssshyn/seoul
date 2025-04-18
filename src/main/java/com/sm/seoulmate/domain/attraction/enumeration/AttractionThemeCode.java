package com.sm.seoulmate.domain.attraction.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttractionThemeCode {
    MUSTVISIT("MV", "필수 관광 명소", "Must-Visit"),
    KCULTURE("KH", "한류 역사와 문화", "K-HistoryCulture"),
    HEALING("HL", "휴식공간", "Healing Spot"),
    RELIGION("RL", "종교와 명상", "Religion and Meditation"),
    TRADITIONAL("TE", "전통 체험", "Traditional Culture Experience"),
    NIGHTVIEW("NV", "야경 & 전망", "Night View"),
    OLDPLACE("OP", "오래가게", "Old Shop"),
    EMOTION("EP", "감성 여행지", "Emotional Place"),
    MUSIUM("MC", "전시 & 현대문화", "Exhibition and Modern Culture"),
    ETC("ET", "기타", "Etc");


    private final String idCode;
    private final String descriptionKor;
    private final String descriptionEng;
}
