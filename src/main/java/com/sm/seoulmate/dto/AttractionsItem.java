package com.sm.seoulmate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionsItem {
    @JsonProperty("POST_SN")
    private String postSn;
    @JsonProperty("LANG_CODE_ID")
    private String langCodeId;
    @JsonProperty("POST_SJ")
    private String postSj;
    @JsonProperty("POST_URL")
    private String postUrl;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("NEW_ADDRESS")
    private String newAddress;
    @JsonProperty("CMMN_TELNO")
    private String cmmnTelNo;
    @JsonProperty("CMMN_FAX")
    private String cmmnFax;
    @JsonProperty("CMMN_HMPG_URL")
    private String cmmnHmpgUrl;
    @JsonProperty("CMMN_USE_TIME")
    private String cmmnUseTime;
    @JsonProperty("CMMN_BSNDE")
    private String cmmnBsnde;
    @JsonProperty("CMMN_RSTDE")
    private String cmmnRstde;
    @JsonProperty("SUBWAY_INFO")
    private String subwayInfo;
    @JsonProperty("TAG")
    private String tag;
    @JsonProperty("BF_DESC")
    private String bfDesc;

//    "POST_SN": "46802",
//            "LANG_CODE_ID": "zh-TW",
//            "POST_SJ": " 2GIL29 GALLERY",
//            "POST_URL": "https://tchinese.visitseoul.net/attractions/2024-2Gil29Gallery/TCPrisi5i?utm_source=seoulopendata&utm_medium=attractions&utm_content=TCPrisi5i",
//            "ADDRESS": " 서울 강남구 신사동 519-22 ",
//            "NEW_ADDRESS": "06034 首爾江南區江南大路158街35號 2GIL29 GALLERY ",
//            "CMMN_TELNO": "+82-2-6203-2015",
//            "CMMN_FAX": "",
//            "CMMN_HMPG_URL": "https://www.2gil29gallery.com/",
//            "CMMN_USE_TIME": "10:00~19:00",
//            "CMMN_BSNDE": "週二~週六",
//            "CMMN_RSTDE": "週日以及週一固定休館\r\n9/16~9/18 中秋假期休館",
//            "SUBWAY_INFO": "距離3號線、新盆唐線新沙站8號出口約460公尺 (步行6分鐘)",
//            "TAG": "GALLERY,江南展覽,美術館,展覽,江南,新沙",
//            "BF_DESC": ""
}
