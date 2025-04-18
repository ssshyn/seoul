package com.sm.seoulmate.domain.attraction.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MountainParkFeignResponse {
    @Schema(description = "키값")
    @JsonProperty("MAIN_KEY")
    private String originId;

    @Schema(description = "장소명")
    @JsonProperty("NAME_KOR")
    private String name;

    @Schema(description = "주소")
    @JsonProperty("ADD_KOR")
    private String address;

    @Schema(description = "전화번호")
    @JsonProperty("TEL")
    private String tel;

    @Schema(description = "교통정보")
    @JsonProperty("TRAFFIC_GUIDE")
    private String subway;
}
