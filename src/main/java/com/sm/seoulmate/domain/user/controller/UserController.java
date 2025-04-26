package com.sm.seoulmate.domain.user.controller;

import com.sm.seoulmate.domain.user.dto.UserInfoResponse;
import com.sm.seoulmate.domain.user.dto.UserUpdateResponse;
import com.sm.seoulmate.domain.user.service.UserService;
import com.sm.seoulmate.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 Controller", description = "유저 정보 관리 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "USER_NOT_FOUND", content = @Content(
                mediaType = "application/json",
                examples = {
                        @ExampleObject(name = "U0002", description = "존재하지 않는 유저입니다.",
                                value = """
                                            {"code": "U0002", "message": "존재하지 않는 유저입니다."}
                                            """)
                }, schema = @Schema(implementation = ErrorResponse.class)
        )),
        @ApiResponse(responseCode = "403", description = "LOGIN_NOT_ACCESS", content = @Content(
                mediaType = "application/json",
                examples = {
                        @ExampleObject(name = "U0001", description = "로그인이 필요한 서비스입니다. 로그인을 해주세요.",
                                value = """
                                            {"code": "U0001", "message": "로그인이 필요한 서비스입니다. 로그인을 해주세요."}
                                            """),
                        @ExampleObject(name = "A0010", description = "만료된 엑세스 토큰입니다.",
                                value = """
                                            {"code": "A0010", "message": "만료된 엑세스 토큰입니다."}
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
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "닉네임 변경", description = "닉네임 변경")
    @PutMapping("/nickname")
    public ResponseEntity<UserInfoResponse> updateLiked(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.updateNickname(nickname));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @ApiResponse(responseCode = "400", description = "PERMISSION_DENIED", content = @Content(
            mediaType = "application/json",
            examples = {
                    @ExampleObject(name = "U0003", description = "수정 권한이 없습니다.",
                            value = """
                                            {"status": U0003, "message": "수정 권한이 없습니다."}
                                            """)
            }, schema = @Schema(implementation = ErrorResponse.class)
    ))
    @DeleteMapping
    public ResponseEntity<UserUpdateResponse> deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new UserUpdateResponse(userId, true));
    }
}
