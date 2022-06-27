package com.triple.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PointStepsAssert {
    public static void 포인트_적립됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 포인트_조회됨(ExtractableResponse<Response> response, int score) {
        assertThat(response.jsonPath().getInt("data.score")).isEqualTo(score);
    }

    public static void 포인트_적립안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 권한_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
