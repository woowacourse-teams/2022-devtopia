package com.woowacourse.levellog.fixture;

import com.woowacourse.levellog.authentication.dto.LoginResponse;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;

public class RestAssuredResponse {

    private final ValidatableResponse response;

    public RestAssuredResponse(ValidatableResponse response) {
        this.response = response;
    }

    public String getLevellogId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/levellogs/")[1];
    }

    public String getTeamId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/api/teams/")[1];
    }

    public Long getMemberId() {
        return response
                .extract()
                .as(LoginResponse.class)
                .getId();
    }

    public String getToken() {
        return response
                .extract()
                .as(LoginResponse.class)
                .getAccessToken();
    }

    public ValidatableResponse getResponse() {
        return response;
    }
}
