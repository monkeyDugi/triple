package com.triple.web.dto;

import com.triple.exception.ExceptionResponse;

public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ExceptionResponse exceptionResponse;

    public ApiResponse(boolean success, T data, ExceptionResponse exceptionResponse) {
        this.success = success;
        this.data = data;
        this.exceptionResponse = exceptionResponse;
    }

    public static <V> ApiResponse<V> success(V data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> fail(ExceptionResponse exceptionResponse) {
        return new ApiResponse<>(false, null, exceptionResponse);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }
}
