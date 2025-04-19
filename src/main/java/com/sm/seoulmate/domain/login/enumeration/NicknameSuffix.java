package com.sm.seoulmate.domain.login.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NicknameSuffix {
    KIMCHI("김치", "Kimchi"),
    HANBOK("한복", "Hanbok"),
    DOKKEBI("도깨비", "Dokkebi"),
    ARIRANG("아리랑", "Arirang"),
    GOMUSIN("고무신", "Gomusin"),
    SSIREUM("씨름", "Ssireum"),
    ONDOL("온돌", "Ondol"),
    SEONBI("선비", "Seonbi"),
    KKACHI("까치", "Kkachi"),
    HAETAE("해태", "Haetae"),
    BUCHAE("부채", "Buchae"),
    HANJI("한지", "Hanji"),
    TAL("탈", "Tal"),
    JEGI("제기", "Jegi"),
    YUT("윷", "Yut"),
    CHILBO("칠보", "Chilbo"),
    GIWA("기와", "Giwa"),
    SURA("수라", "Sura"),
    TTEOK("떡", "Tteok"),
    HWARO("화로", "Hwaro"),
    YEOT("엿", "Yeot"),
    TAP("탑", "Tap"),
    SAMBE("삼베", "Sambe"),
    SOTDAE("솟대", "Sotdae"),
    AGUUNGI("아궁이", "Aguungi"),
    DUREBAK("두레박", "Durebak"),
    MARU("마루", "Maru"),
    BARANG("바랑", "Barang"),
    BEOSEON("버선", "Beoseon"),
    GONU("고누", "Gonu");

    private final String kor;
    private final String eng;
}
