package com.letmeclean.exception.member;

public class InvalidPasswordException extends RuntimeException {

    private static final InvalidPasswordException INSTANCE = new InvalidPasswordException();

    public static InvalidPasswordException getInstance() {
        return INSTANCE;
    }
}
