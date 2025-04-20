package com.sm.seoulmate.util;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserInfoUtil {
    private static LoginInfo loginInfo() {
        if(StringUtils.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "anonymousUser")) {
            return null;
        }
        return (LoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    /**
     * 유저 객체
     */
    public static LoginInfo getUser() {
        return loginInfo();
    }

    /**
     * 유저 아이디 (이메일)
     */
    public static String getUserId() {
        LoginInfo user = loginInfo();

        if(user == null) {
            return null;
        }
        return user.getUserId();
    }

    /**
     * 유저 닉네임
     */
    public static String getNickname(LanguageCode languageCode) {
        LoginInfo user = loginInfo();

        if(user == null) {
            return null;
        }

        return languageCode.equals(LanguageCode.KOR) ? user.getNicknameKor() : user.getNicknameEng();
    }
}
