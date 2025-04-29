package com.sm.seoulmate.domain.challenge.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum CulturePeriod {
    ACTIVE("오늘 참여 가능", 2),
    THIS_WEEKEND("이번 주까지", 1),
    THIS_MONTH("이번 달 개최", 3),
    SCHEDULED("예정된 행사", 4),
    PAST("지난 행사", 5);

    private final String description;
    private final Integer displayRank;

    public static CulturePeriod getTodayPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();

        if (now.isBefore(startDate)) {
            return SCHEDULED;
        } else if (now.isAfter(endDate)) {
            return PAST;
        } else {
            // 오늘이 기간 안에 포함됨
            if (isThisWeek(endDate)) {
                return CulturePeriod.THIS_WEEKEND;
            } else if (isThisMonth(endDate)) {
                return CulturePeriod.THIS_MONTH;
            } else {
                return CulturePeriod.ACTIVE;
            }
        }
    }

    private static boolean isThisWeek(LocalDate date) {
        LocalDate now = LocalDate.now();
        // 이번 주의 월요일
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
        // 이번 주의 일요일
        LocalDate endOfWeek = now.with(java.time.DayOfWeek.SUNDAY);
        return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
    }

    private static boolean isThisMonth(LocalDate date) {
        LocalDate now = LocalDate.now();
        return date.getYear() == now.getYear() && date.getMonth() == now.getMonth();
    }
}
