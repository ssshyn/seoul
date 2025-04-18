package com.sm.seoulmate.domain.attraction.feign.dto.tourApiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordResponse {
    @JsonProperty("response")
    private TourKeywordResponse tourKeywordResponse;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TourKeywordResponse {
        @JsonProperty("body")
        private KeywordBody keywordBody;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordBody{
        private KeywordItems items;
        private Integer totalCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordItems{
        List<KeywordItem> item;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordItem{
        @JsonProperty("contentid")
        private String contentId;
        @JsonProperty("contenttypeid")
        private String contentTypeId;
        @JsonProperty("originimgurl")
        private String imageUrl;
        @JsonProperty("overview")
        private String description;
    }
}
