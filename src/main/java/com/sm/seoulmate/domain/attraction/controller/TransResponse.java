package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String subway;

    public TransResponse(AttractionInfo attractionInfo) {
        this.id = attractionInfo.getId();
        this.name = attractionInfo.getName();
        this.description = attractionInfo.getDescription();
        this.address = attractionInfo.getAddress();
        this.subway = attractionInfo.getSubway();
    }
}
