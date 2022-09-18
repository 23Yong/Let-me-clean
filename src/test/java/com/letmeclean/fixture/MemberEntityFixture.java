package com.letmeclean.fixture;

import com.letmeclean.model.member.Member;

public class MemberEntityFixture {

    public static Member get() {
        return Member.of(
                "23Yong@email.com",
                "password123!",
                "username",
                "23Yong",
                "010-1234-1234"
        );
    }
}
