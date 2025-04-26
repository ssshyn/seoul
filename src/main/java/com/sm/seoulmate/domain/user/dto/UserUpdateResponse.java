package com.sm.seoulmate.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateResponse(
        @Schema(description = "유저 id")
        Long id,
        @Schema(description = "처리 여부")
        Boolean isProcessed
){
}
