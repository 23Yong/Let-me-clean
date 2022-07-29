package com.letmeclean.global.exception.member;

public class MemberNotFoundException extends RuntimeException {

    private static final MemberNotFoundException INSTANCE = new MemberNotFoundException();

    public static MemberNotFoundException getInstance() {
        return INSTANCE;
    }
}
