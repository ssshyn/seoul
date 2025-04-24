package com.sm.seoulmate.domain.user.controller;

import com.sm.seoulmate.domain.user.dto.LoginRequest;
import com.sm.seoulmate.domain.user.dto.LoginResponse;
import com.sm.seoulmate.domain.user.dto.TokenRefreshRequest;
import com.sm.seoulmate.domain.user.service.LoginService;
import com.sm.seoulmate.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 Controller", description = "인증 관리 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @Operation(
            summary = "로그인",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "A0002", description = "잘못된 유형의 토큰입니다.",
                                    value = """
                                            {"code": "A0002", "message": "잘못된 유형의 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0003", description = "만료된 토큰입니다.",
                                    value = """
                                            {"code": "A0003", "message": "만료된 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0005", description = "유효하지 않은 토큰입니다.",
                                    value = """
                                            {"code": "A0005", "message": "유효하지 않은 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0006", description = "인증정보가 등록되지 않았습니다. 서버에 문의해주세요.",
                                    value = """
                                            {"code": "A0006", "message": "인증정보가 등록되지 않았습니다. 서버에 문의해주세요."}
                                            """),
                            @ExampleObject(name = "A0009", description = "인증 키 파싱 중 오류가 발생하였습니다.",
                                    value = """
                                            {"code": "A0009", "message": "인증 키 파싱 중 오류가 발생하였습니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest condition) {
        LoginResponse jwt = loginService.processLogin(condition);
        return ResponseEntity.ok(jwt);
    }

    @Operation(
            summary = "리프레시 토큰",
            security = @SecurityRequirement(name = "") // 빈 security 설정
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "A0002", description = "잘못된 유형의 토큰입니다.",
                                    value = """
                                            {"code": "A0002", "message": "잘못된 유형의 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0003", description = "만료된 토큰입니다.",
                                    value = """
                                            {"code": "A0003", "message": "만료된 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0005", description = "유효하지 않은 토큰입니다.",
                                    value = """
                                            {"code": "A0005", "message": "유효하지 않은 토큰입니다."}
                                            """),
                            @ExampleObject(name = "A0006", description = "인증정보가 등록되지 않았습니다. 서버에 문의해주세요.",
                                    value = """
                                            {"code": "A0006", "message": "인증정보가 등록되지 않았습니다. 서버에 문의해주세요."}
                                            """),
                            @ExampleObject(name = "A0009", description = "인증 키 파싱 중 오류가 발생하였습니다.",
                                    value = """
                                            {"code": "A0009", "message": "인증 키 파싱 중 오류가 발생하였습니다."}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "500", description = "INTERNAL SERVER ERROR",
                                    value = """
                                            {"status": 500, "message": "INTERNAL SERVER ERROR"}
                                            """)
                    }, schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(loginService.refreshAccessToken(request));
    }
}
