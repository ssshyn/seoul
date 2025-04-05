package com.sm.seoulmate.domain.attraction.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum LanguageCode {
    ENG("en"),
    KOR("ko");

    final String short_code;
}
