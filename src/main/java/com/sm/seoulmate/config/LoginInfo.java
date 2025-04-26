package com.sm.seoulmate.config;

import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private Long id;
    private String email;
    private LoginType loginType;
    private String nickname;

    public static LoginInfo of(User user) {
        return LoginInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .loginType(user.getLoginType())
                .nickname(user.getNickname())
                .build();
    }
}
