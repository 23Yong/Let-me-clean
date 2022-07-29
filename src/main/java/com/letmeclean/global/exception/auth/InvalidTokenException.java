package com.letmeclean.global.exception.auth;

public class InvalidTokenException extends RuntimeException {

    private static final InvalidTokenException INSTANCE = new InvalidTokenException();

    public static InvalidTokenException getInstance() {
        return INSTANCE;
    }
}
