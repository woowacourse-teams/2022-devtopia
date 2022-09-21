package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikeNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikesAlreadyExistException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("InterviewQuestionSearchController 의")
class InterviewQuestionSearchControllerTest extends ControllerTest {

    private static final String BASE_SNIPPET_PATH = "interview-question-search/likes/exception/";

    @Test
    @DisplayName("이미 좋아요한 인터뷰 질문을 좋아요 한 경우 예외를 던진다.")
    void pressLike_alreadyLike_exception() throws Exception {
        willThrow(new InterviewQuestionLikesAlreadyExistException(DebugMessage.init()))
                .given(interviewQuestionService)
                .pressLike(1L, 1L);

        // when
        final ResultActions perform = requestPost("/api/interview-questions/" + 1L + "/like");

        // then
        perform.andExpectAll(
                status().isBadRequest(),
                jsonPath("message").value("인터뷰 질문에 대한 좋아요가 이미 존재합니다.")
        );

        // docs
        perform.andDo(document(BASE_SNIPPET_PATH + "already-like"));
    }

    @Test
    @DisplayName("좋아요하지 않은 인터뷰 질문을 좋아요 취소한 경우 예외를 던진다.")
    void cancelLike_notFoundInterviewQuestion_exception() throws Exception {
        willThrow(new InterviewQuestionLikeNotFoundException(DebugMessage.init()))
                .given(interviewQuestionService)
                .cancelLike(1L, 1L);

        // when
        final ResultActions perform = requestDelete("/api/interview-questions/1/like");

        // then
        perform.andExpectAll(
                status().isNotFound(),
                jsonPath("message").value("인터뷰 질문을 '좋아요'하지 않았습니다.")
        );

        // docs
        perform.andDo(document(BASE_SNIPPET_PATH + "not-found-cancel"));
    }
}
