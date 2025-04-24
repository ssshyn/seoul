package com.sm.seoulmate.util;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public static Optional<LoginInfo> getUser() {
        return Optional.ofNullable(loginInfo());
    }

    /**
     * 유저 아이디 (아이디)
     */
    public static Long getUserId() {
        LoginInfo user = loginInfo();

        if(user == null) {
            return null;
        }
        return user.getId();
    }

    /**
     * 유저 아이디 (이메일)
     */
    public static String getUserEmail() {
        LoginInfo user = loginInfo();

        if(user == null) {
            return null;
        }
        return user.getEmail();
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
