package com.letmeclean.dto.member.request;

import com.letmeclean.dto.member.MemberDto;

public record MemberModifyRequest(
        String username,
        String nickname,
        String tel
) {

    public static MemberModifyRequest of(String username, String nickname, String tel) {
        return new MemberModifyRequest(username, nickname, tel);
    }

    public MemberDto toDto(String email) {
        return MemberDto.of(
                email,
                null,
                username,
                nickname,
                tel
        );
    }
}
