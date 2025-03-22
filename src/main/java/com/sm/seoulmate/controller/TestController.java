package com.sm.seoulmate.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @Operation(summary = "테스트입니다.", description = "ci/cd swagger Test")
    @GetMapping
    public String test() {
        return "hi";
    }
    @PostMapping
    public void post1() {}
    @PutMapping
    public void put1() {}
    @DeleteMapping
    public void delete1() {}
}
