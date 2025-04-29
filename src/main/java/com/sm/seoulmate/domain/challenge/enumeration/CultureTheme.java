package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

@Getter
@AllArgsConstructor
public enum CultureTheme {
    ART("전시/미술"),
    FESTIVAL_HISTORY("축제-전통/역사"),
    FESTIVAL_NATURE("축제-자연/경관"),
    GUGAK("국악"),
    CONCERT("콘서트");

    private final String description;

    public String getPrefixTitle(CulturePeriod culturePeriod, String title) {
        String prefix = "";
        switch (this) {
            case ART -> {
                if (culturePeriod.equals(CulturePeriod.ACTIVE)) {
                    prefix = "지금 열린 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_WEEKEND)) {
                    prefix = "이번 주 마감 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_MONTH)) {
                    prefix = "이달의 추천 ";
                } else {
                    prefix = "곧 열리는 ";
                }
            }
            case FESTIVAL_HISTORY, FESTIVAL_NATURE -> {
                if (culturePeriod.equals(CulturePeriod.ACTIVE)) {
                    prefix = "오늘 가기 좋은 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_WEEKEND)) {
                    prefix = "이번 주까지 즐기는 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_MONTH)) {
                    prefix = "이번 달 꼭 가야할 ";
                } else {
                    prefix = "곧 열리는 ";
                }
            }
            case GUGAK -> {
                if (culturePeriod.equals(CulturePeriod.ACTIVE)) {
                    prefix = "오늘의 국악 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_WEEKEND)) {
                    prefix = "이번 주만 볼 수 있는 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_MONTH)) {
                    prefix = "이달의 추천 국악 ";
                } else {
                    prefix = "기대되는 ";
                }
            }
            case CONCERT -> {
                if (culturePeriod.equals(CulturePeriod.ACTIVE)) {
                    prefix = "오늘의 콘서트 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_WEEKEND)) {
                    prefix = "이번 주만 볼 수 있는 ";
                } else if (culturePeriod.equals(CulturePeriod.THIS_MONTH)) {
                    prefix = "이번 달 추천 콘서트 ";
                } else {
                    prefix = "기대되는 ";
                }
            }
        }

        return prefix + title;
    }
}
