package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.service.BatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "배치_세팅 사용금지", description = "누르지마세요")
@RestController
@RequestMapping("batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    @GetMapping("setData")
    public void setData() throws Exception { batchService.setAttractionData(); }
    @GetMapping("setLocation")
    public void setLocation() throws Exception { batchService.setCooperation(); }
    @GetMapping("setInfo")
    public void setInfo() { batchService.setTourApiInfo(); }
    @GetMapping("setTranslation")
    public void setTranslation() {}
}
