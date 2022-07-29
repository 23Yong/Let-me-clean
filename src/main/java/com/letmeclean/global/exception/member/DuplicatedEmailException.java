package com.letmeclean.global.exception.member;

public class DuplicatedEmailException extends RuntimeException {

    private static final DuplicatedEmailException INSTANCE = new DuplicatedEmailException();

    public static DuplicatedEmailException getInstance() {
        return INSTANCE;
    }
}
