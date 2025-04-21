package com.sm.seoulmate.domain.attraction.dto;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchAttraction {
    @Schema(description = "관광지 id")
    Long id;
    @Schema(description = "관광지명")
    String name;
    @Schema(description = "관광지 주소")
    String address;

    public SearchAttraction(AttractionId id, AttractionInfo info) {
        this.id = id.getId();
        this.name = info.getName();
        this.address = info.getAddress();
    }
}
