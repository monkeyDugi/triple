package com.triple.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public enum ExceptionCode {
    MEMBER_INVALID(BAD_REQUEST, "MEMBER_001", "존재하지 않는 사용자입니다."),
    MEMBER_AUTHORIZATION(FORBIDDEN, "MEMBER_002", "권한이 불충분합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
