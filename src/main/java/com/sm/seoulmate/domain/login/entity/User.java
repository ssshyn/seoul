package com.sm.seoulmate.domain.login.entity;

import com.sm.seoulmate.domain.login.enumeration.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
    private String nickname;
}
