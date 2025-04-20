package com.sm.seoulmate.util;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserInfoUtil {
    /**
     * 유저 객체
     */
    public static LoginInfo getUser() {
        return (LoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 유저 아이디 (이메일)
     */
    public static String getUserId() {
        LoginInfo user = (LoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getUserId();
    }

    /**
     * 유저 닉네임
     */
    public static String getNickname(LanguageCode languageCode) {
        LoginInfo user = (LoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return languageCode.equals(LanguageCode.KOR) ? user.getNicknameKor() : user.getNicknameEng();
    }
}
