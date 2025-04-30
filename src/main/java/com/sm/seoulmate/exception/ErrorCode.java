package com.sm.seoulmate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * Bad Request
     */
    ATTRACTION_NOT_FOUND(400, "R0001", "관광지 정보를 조회할 수 없습니다. 다시 확인해 주세요."),
    CHALLENGE_NOT_FOUND(400, "R0002", "챌린지 정보를 조회할 수 없습니다. 다시 확인해 주세요."),
    CHALLENGE_THEME_NOT_FOUND(400, "R0003", "테마 정보를 조회할 수 없습니다. 다시 확인해 주세요."),
    COMMENT_NOT_FOUND(400, "R0007", "댓글 정보를 조회할 수 없습니다. 다시 확인해 주세요."),
    REQUIRED_PARAMETER(400, "R0004", "필수값이 입력되지 않았습니다. 다시 확인해 주세요."),
    STATUS_NOT_ALLOWED(400, "R0005", "챌린지 완료 처리가 불가합니다. 다시 확인해 주세요."),
    MAX_SIZE(400, "R0006", "파라미터 최대 값을 초과하였습니다."),
    WRONG_PARAMETER(400, "R0008", "잘못된 요청 데이터입니다. 다시 확인해 주세요."),
    NICK_DUPLICATE(400, "R0009", "닉네임이 중복되었습니다."),

    /**
     * 유저 에러
     */
    LOGIN_NOT_ACCESS(403, "U0001", "로그인이 필요한 서비스입니다. 로그인을 해주세요."),
    USER_NOT_FOUND(401, "U0002", "존재하지 않는 유저입니다."),
    PERMISSION_DENIED(400, "U0003", "수정 권한이 없습니다."),

    /**
     * 인증 에러
     */
    NO_TOKEN(400, "A0001", "토큰이 존재하지 않습니다."),
    NOT_MATCH_CATEGORY(400, "A0002", "잘못된 유형의 토큰입니다."),
    TOKEN_EXPIRED(403, "A0003", "만료된 토큰입니다."),
    ACCEESS_TOKEN_EXPIRED(403, "A0010", "만료된 엑세스 토큰입니다."),
    TOKEN_NOT_EXPIRED(403, "A0004", "아직 토큰이 만료되지 않았습니다."),
    INVALID_TOKEN(401, "A0005", "유효하지 않은 토큰입니다."),
    UNAUTHENTICATED_USER(401, "A0006", "인증정보가 등록되지 않았습니다. 서버에 문의해주세요."),
    FAIL_CREATE_REVOKE_TOKEN(500, "A0007", "revoke Token 생성에 실패했습니다."),
    INVALID_REFRESH_TOKEN(401, "A0008", "유효하지 않은 refresh token입니다. 재로그인 해주세요."),
    KEY_PARSING_ERROR(401, "A0009", "인증 키 파싱 중 오류가 발생하였습니다."),

    /**
     * 서버 에러
     */
    SERVER_ERROR(500, "SY001", "알 수 없는 에러가 발생했습니다. 서버에 문의해주세요."),
    INVALID_DATA_FORMAT(400, "SY002", "잘못된 요청 데이터입니다."),
    DUPLICATED_DATA(409, "SY003", "데이터 중복이 발생했습니다. 서버에 문의해주세요.");

    private final int status;
    private final String code;
    private final String message;
}
