package com.sm.seoulmate.domain.attraction.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverMapResponse {
    private String status;
    private List<Address> addresses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String roadAddress;
        private String englishAddress;
        private String x;
        private String y;
    }
}