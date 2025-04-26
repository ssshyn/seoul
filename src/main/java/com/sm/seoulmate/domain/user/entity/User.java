package com.sm.seoulmate.domain.user.entity;

import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.challenge.entity.ChallengeLikes;
import com.sm.seoulmate.domain.challenge.entity.ChallengeStatus;
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
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    private String nickname;

    // 댓글 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // 장소 스탬프
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VisitStamp> visitStamps = new ArrayList<>();

    // 관광지 찜 여부
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AttractionLikes> attractionLikes = new ArrayList<>();

    // 챌린지 상태
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeStatus> challengeStatuses = new ArrayList<>();

    // 챌린지 찜 여부
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeLikes> challengeLikes = new ArrayList<>();

    public static User of(String userId, LoginType loginType, String nickname) {
        return User.builder()
                .email(userId)
                .loginType(loginType)
                .nickname(nickname)
                .build();
    }
}
