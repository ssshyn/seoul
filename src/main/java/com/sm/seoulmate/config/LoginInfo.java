package com.sm.seoulmate.config;

import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    @Id
    private String userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    private String nicknameKor;

    @Column(nullable = false)
    private String nicknameEng;

    public static LoginInfo of(User user) {
        return LoginInfo.builder()
                .userId(user.getUserId())
                .loginType(user.getLoginType())
                .nicknameKor(user.getNicknameKor())
                .nicknameEng(user.getNicknameEng())
                .build();
    }
}
