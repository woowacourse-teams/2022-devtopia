package com.woowacourse.levellog.authentication.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class LoginResponse {

    private Long id;
    private String accessToken;
    private String profileUrl;
}
