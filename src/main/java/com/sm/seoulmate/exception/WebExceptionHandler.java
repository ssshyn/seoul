package com.sm.seoulmate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponse> handlerTokenException(ErrorException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        errorLogging(ex.getStackTrace(), errorCode, 0);
        // 전체 예외 로그 출력 (stacktrace 포함)
        log.error("예외 발생: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorCode errorCode = ErrorCode.DUPLICATED_DATA;
        errorLogging(ex.getStackTrace(), errorCode, 0);
        // 전체 예외 로그 출력 (stacktrace 포함)
        log.error("예외 발생: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleRemainException(Exception ex) {
        ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        errorLogging(ex.getStackTrace(), errorCode, 1);
        // 전체 예외 로그 출력 (stacktrace 포함)
        log.error("예외 발생: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    private void errorLogging(StackTraceElement[] stackTrace, ErrorCode errorCode, Integer type) {
        String callerClassName = "Unknown";
        String callerMethodName = "Unknown";

        if (stackTrace.length > 2) {
            callerClassName = stackTrace[2].getClassName();
            callerMethodName = stackTrace[2].getMethodName();
        }

        String stackTraceString = "";
        if (type != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(Arrays.toString(stackTrace)).append("\n");
            for (StackTraceElement element : stackTrace) {
                sb.append("\tat ").append(element.toString()).append("\n");
            }
            stackTraceString = sb.toString();
        }

        if (type == 0) {
            log.error("\n에러 발생 위치: {}.{}\n에러 코드: {}", callerClassName, callerMethodName, errorCode);
        } else {
            log.error("\n에러 발생 위치: {}.{}\n에러 코드: {}\n에러 메세지: {}", callerClassName, callerMethodName, errorCode, stackTraceString);
        }
    }

}
