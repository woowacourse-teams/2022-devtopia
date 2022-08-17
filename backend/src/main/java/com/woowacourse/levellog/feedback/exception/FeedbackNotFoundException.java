package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "존재하지 않는 피드백입니다.";

    public FeedbackNotFoundException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
