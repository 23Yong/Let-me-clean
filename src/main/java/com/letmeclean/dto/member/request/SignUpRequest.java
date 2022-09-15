package com.letmeclean.dto.member.request;

import com.letmeclean.dto.member.MemberDto;

public record SignUpRequest(
        String email,
        String password,
        String username,
        String nickname,
        String tel
) {

    public static SignUpRequest of(String email, String password, String username, String nickname, String tel) {
        return new SignUpRequest(
                email,
                password,
                username,
                nickname,
                tel
        );
    }

    public MemberDto toDto() {
        return MemberDto.of(
                email,
                password,
                username,
                nickname,
                tel
        );
    }
}
