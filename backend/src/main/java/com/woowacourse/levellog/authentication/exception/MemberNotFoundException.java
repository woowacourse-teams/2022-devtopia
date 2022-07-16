package com.woowacourse.levellog.authentication.exception;

/**
 * 존재하지 않는 멤버를 조회하는 경우 발생하는 예외입니다.
 */
public class MemberNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "멤버가 존재하지 않습니다.";

    public MemberNotFoundException(final String message) {
        super(message);
    }

    public MemberNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
