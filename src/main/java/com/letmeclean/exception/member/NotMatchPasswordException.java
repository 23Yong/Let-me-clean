package com.letmeclean.exception.member;

public class NotMatchPasswordException extends RuntimeException {

    private static final NotMatchPasswordException INSTANCE = new NotMatchPasswordException();

    public static NotMatchPasswordException getInstance() {
        return INSTANCE;
    }
}
