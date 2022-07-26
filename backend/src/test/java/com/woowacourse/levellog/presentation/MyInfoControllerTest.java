package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MyInfoControllerTest extends ControllerTest {

    // FIXME : 팀 API 구현 후 수정
    @Disabled
    @Test
    void findAllMyFeedback() {
    }

    @Nested
    @DisplayName("updateNickname 메서드는 ")
    class updateNickname {

        @Test
        @DisplayName("닉네임에 50자를 초과한 문자열이 들어올 경우 예외를 던진다.")
        void nicknameInvalidLength_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("123");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            doThrow(new InvalidFieldException("닉네임은 50자 이하여야합니다."))
                    .when(memberService)
                    .updateNickname(any(), any());
            final String invalidNickname = "a".repeat(51);

            final NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto(invalidNickname);
            final String requestContent = objectMapper.writeValueAsString(nicknameUpdateDto);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/myInfo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer: token"))
                    .andDo(print());

            // then
            perform.andExpect(
                    status().isBadRequest()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("닉네임에 null 또는 빈 값이 들어온 경우 예외를 던진다.")
        void nicknameNullAndEmpty_Exception(final String invalidNickname) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("123");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto(invalidNickname);
            final String requestContent = objectMapper.writeValueAsString(nicknameUpdateDto);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/myInfo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer: token"))
                    .andDo(print());

            // then
            perform.andExpect(
                    status().isBadRequest()
            );
        }
    }
}
