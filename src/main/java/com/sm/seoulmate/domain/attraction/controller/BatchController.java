package com.sm.seoulmate.domain.attraction.controller;

import com.sm.seoulmate.domain.attraction.dto.AttractionRequest;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.service.BatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "배치_세팅 사용금지", description = "누르지마세요")
@RestController
@RequestMapping("batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;
    @GetMapping("getAttractionData")
    public List<TransResponse> getAttractionData() {
        return batchService.getChallengeAttractionInfo();
    }

    @PostMapping("setTrans")
    public Boolean setTrans(@RequestBody List<TransResponse> attractionInfo) {
        return batchService.setTrans(attractionInfo);
    }

    @GetMapping("setData")
    public void setData() throws Exception { batchService.setAttractionData(); }
    @GetMapping("setLocation")
    public void setLocation() throws Exception { batchService.setCooperation(); }
    @GetMapping("setInfo")
    public void setInfo() { batchService.setTourApiInfo(); }
    @GetMapping("setChallengeImage")
    public boolean setImage(@RequestParam("id") Long id, @RequestParam("img") String img) { return batchService.setChallengeImage(id, img); }
    @PostMapping("setAttraction")
    public boolean setAttraction(@RequestBody AttractionRequest attractionRequest) {
        return batchService.setAttraction(attractionRequest);
    }
    @GetMapping("setCulture")
    public void setCulture() { batchService.setCulture(); }
}
