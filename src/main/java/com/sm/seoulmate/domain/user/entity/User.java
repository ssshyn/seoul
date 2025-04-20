package com.sm.seoulmate.domain.user.entity;

import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    private String userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    private String nicknameKor;
    @Column(nullable = false)
    private String nicknameEng;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;

    // 댓글 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static User of(String userId, LoginType loginType, String nicknameKor, String nicknameEng) {
        return User.builder()
                .userId(userId)
                .loginType(loginType)
                .nicknameKor(nicknameKor)
                .nicknameEng(nicknameEng)
                .languageCode(LanguageCode.KOR)
                .build();
    }
}
