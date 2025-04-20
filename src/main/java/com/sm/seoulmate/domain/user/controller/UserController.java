package com.sm.seoulmate.domain.user.controller;

import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 Controller", description = "유저 정보 관리 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "언어 변경", description = "전체조회 페이징 처리")
    @PutMapping("/language")
    public ResponseEntity<?> setDefaultLanguage(@RequestParam(name = "languageCode") LanguageCode languageCode) {
        userService.setDefaultLanguage(languageCode);
        return ResponseEntity.ok().build();
    }
}
