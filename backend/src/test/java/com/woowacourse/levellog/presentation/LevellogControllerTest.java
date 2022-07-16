package com.woowacourse.levellog.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.dto.LevellogCreateRequest;
import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

// FIXME : 팀 API 구현 후 수정
@Disabled
@WebMvcTest(LevellogController.class)
@DisplayName("LevellogController의")
class LevellogControllerTest extends ControllerTest {

    // FIXME : 팀 API 구현 후 수정
    @Test
    void test() {
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("내용으로 공백이나 null이 들어오면 예외를 던진다.")
        void nameNullOrEmpty_Exception(final String content) throws Exception {
            // given
            final LevellogCreateRequest request = new LevellogCreateRequest(content);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("내용으로 공백이나 null이 들어오면 예외를 던진다.")
        void nameNullOrEmpty_Exception(final String content) throws Exception {
            // given
            final LevellogCreateRequest request = new LevellogCreateRequest(content);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/levellogs/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
