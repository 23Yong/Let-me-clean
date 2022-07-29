package com.letmeclean.global.exception.member;

public class DuplicatedNicknameException extends RuntimeException {

    private static final DuplicatedNicknameException INSTANCE = new DuplicatedNicknameException();

    public static DuplicatedNicknameException getInstance() {
        return INSTANCE;
    }
}
