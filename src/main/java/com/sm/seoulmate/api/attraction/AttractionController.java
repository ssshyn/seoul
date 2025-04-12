package com.sm.seoulmate.api.attraction;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("attraction")
@RequiredArgsConstructor
public class AttractionController {

    private final BatchService batchService;

    @GetMapping("setData")
    public void setData() throws Exception {
        batchService.setAttractionData();
    }

    @GetMapping
    public List<AttractionId> getAttractionList() {
        return batchService.getAttractions();
    }

    @GetMapping("test")
    public String test() {return "hi";}

}
