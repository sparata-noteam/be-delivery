package com.sparta.bedelivery.global.exception;

import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.global.response.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 404 Not Found 예외 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseData<String> handleNotFoundException() {
        return ApiResponseData.failure(404, "요청한 API를 찾을 수 없습니다.");
    }


    // 403 Forbidden 예외 처리
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponseData<String> handleAccessDeniedException() {
        return ApiResponseData.failure(403, "접근 권한이 없습니다.");
    }

    // 데이터 무결성 오류 (중복 키 제약 조건 위반)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // HTTP 400 - 잘못된 요청
    public ApiResponseData<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        return ApiResponseData.failure(400, message);
    }

    // 공통적인 500 Internal Server Error 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseData<Object> handleGlobalException(Exception ex) {
        log.error("서버 내부 오류 발생", ex);

        // 개발 중에는 상세 에러 정보 반환, 운영 환경에서는 기본 메시지만 반환
        boolean isDevMode = true; // 개발 환경 여부 (운영에서는 false로 설정)
        if (isDevMode) {
            // 스택 트레이스를 문자열로 변환하여 반환
            List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .limit(5) // 너무 길면 5줄까지만 반환
                    .toList();
            return ApiResponseData.failure(500, "서버 내부 오류가 발생했습니다.", stackTrace);
        } else {
            return ApiResponseData.failure(500, "서버 내부 오류가 발생했습니다.");
        }
    }

    // GlobalExceptionHandler 수정
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseData<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponseData.failure(404, ex.getMessage());  // 예외 메시지 그대로 반환
    }




}

