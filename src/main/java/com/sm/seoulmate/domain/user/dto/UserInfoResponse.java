package com.sm.seoulmate.domain.user.dto;

import com.sm.seoulmate.domain.user.enumeration.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    @Schema(description = "유저ID")
    private Long id;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "닉네임")
    private String nickname;
}
