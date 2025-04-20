package com.sm.seoulmate.domain.user.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {
    GOOGLE("구글"),
    APPLE("애플"),
    FACEBOOK("페이스북");

    private String description;
}
