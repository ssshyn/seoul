package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewNightSpot {
    @JsonProperty("list_total_count")
    private int listTotalCount;
    @JsonProperty("RESULT")
    private Result result;
    @JsonProperty("row")
    private List<NightSpot> row;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NightSpot {
        @JsonProperty("NUM")
        private Long num;
        @JsonProperty("SUBJECT_CD")
        private String subjectCd;
        @JsonProperty("TITLE")
        private String title;
        @JsonProperty("ADDR")
        private String address;
        @JsonProperty("LA")
        private String locationY;
        @JsonProperty("LO")
        private String locationX;
        @JsonProperty("TEL_NO")
        private String telNo;
        @JsonProperty("URL")
        private String homageUrl;
        @JsonProperty("OPERATING_TIME")
        private String operatingTime;
        @JsonProperty("FREE_YN")
        private String freeYn;
        @JsonProperty("ENTR_FREE")
        private String entrFree;
        @JsonProperty("CONTENTS")
        private String contents;
        @JsonProperty("SUBWAY")
        private String subwayInfo;
        @JsonProperty("BUS")
        private String busInfo;
        @JsonProperty("PARKING_INFO")
        private String parkingInfo;
        @JsonProperty("REG_DATE")
        private String regDate;
        @JsonProperty("MOD_DATE")
        private String modDate;
//        "NUM": 1,
//                "SUBJECT_CD": "문화/체육",
//                "TITLE": "남산서울타워",
//                "ADDR": "서울특별시 용산구 남산공원길 105",
//                "LA": "37.5511225714939",
//                "LO": "126.987867837993",
//                "TEL_NO": "02-3455-9277, 9288",
//                "URL": "https://www.seoultower.co.kr/",
//                "OPERATING_TIME": "연중무휴 * 타워내 개별시설 운영시간 상이",
//                "FREE_YN": "무료",
//                "ENTR_FEE": "남산공원 입장료 없음(전망대 등 개별 이용시설 요금 별도)",
//                "CONTENTS": "\u003Cp\u003E남산서울타워는 효율적인 방송전파 송수신과 한국의 전통미를 살린 관광 전망시설의 기능을 겸비한 국내 최초의 종합전파 탑으로 방송문화와 관광산업의 미래를 위해 건립되었습니다.\u003C/p\u003E\r\n\u003Cp\u003E세계 유명한 종합 탑들이 그 나라 또는 그 도시의 상징적인 존재가 된 것처럼 남산서울타워 역시 지난 40여 년간 대한민국의 대표적인 관광지이자 서울의 상징물 역할을 해왔습니다.\u003C/p\u003E\r\n\u003Cp\u003E남산서울타워는 서울 시내 전 지역에서 바라보이는 탑의 높이와 독특한 구조, 형태 등으로 인하여 시민의 관심과 사랑의 대상이 되었고, 내외국인들이 즐겨 찾는 제1의 관광 명소로서의 위치를 확고히 하고 있습니다. 최근에는 한류 바람을 몰고 온 각종 예능, 드라마의 촬영지로 이름이 높아지면서 내외국인 관광객들이 발길이 끊이지 않는 곳입니다.\u003C/p\u003E\r\n\u003Cp\u003E(출처 : 남산서울타워 홈페이지)\u003C/p\u003E\r\n\u003Cp\u003E전망대 뿐만 아니라 남산공원에서 산책하면서, 남산케이블카를 이용하면서, 남산둘레길 트레킹하면서 서울의 야경도 다양하게 감상할 수 있습니다.\u003C/p\u003E",
//                "SUBWAY": "지하철 4호선 회현역 3번 출구 도보 이용",
//                "BUS": "남산순환버스 01A번, 01B번 이용  * 남산공원 생태 환경 보호 일환으로 승용차량 통행 제한",
//                "PARKING_INFO": "",
//                "REG_DATE": "2024-06-05 00:00:00.0",
//                "MOD_DATE": "2024-06-24 16:59:59.0"

    }
}
