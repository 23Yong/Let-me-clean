package com.letmeclean.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LetMeCleanException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public LetMeCleanException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        message = null;
    }
}
