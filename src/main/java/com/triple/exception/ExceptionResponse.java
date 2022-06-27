package com.triple.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private final HttpStatus status;
    private final String code;
    private final String message;

    private ExceptionResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ExceptionResponse createBusiness(ExceptionCode exceptionCode) {
        return new ExceptionResponse(exceptionCode.getStatus(), exceptionCode.getCode(), exceptionCode.getMessage());
    }

    public static ExceptionResponse createBind(FieldErrorDetail fieldErrorDetail) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, fieldErrorDetail.getCode(), fieldErrorDetail.getMessage());
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
