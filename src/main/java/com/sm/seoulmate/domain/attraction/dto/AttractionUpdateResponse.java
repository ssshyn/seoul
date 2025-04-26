package com.sm.seoulmate.domain.attraction.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AttractionUpdateResponse(
        @Schema(description = "관광지 id")
        Long id,
        @Schema(description = "처리 여부")
        Boolean isProcessed
){
}
