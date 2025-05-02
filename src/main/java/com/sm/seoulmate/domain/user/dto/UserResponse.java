package com.sm.seoulmate.domain.user.dto;

import com.sm.seoulmate.domain.user.enumeration.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @Schema(description = "유저ID")
    private Long id;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "로그인 타입")
    private LoginType loginType;
    @Schema(description = "찜한 장소 수")
    private Integer likedCount;
    @Schema(description = "배지 수")
    private Integer badgeCount;
    @Schema(description = "댓글 수")
    private Integer commentCount;
}
