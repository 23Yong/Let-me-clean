package com.letmeclean.dto.member.response;

import com.letmeclean.dto.member.MemberDto;

public record MemberResponse(
        Long id,
        String email,
        String password,
        String username,
        String nickname,
        String tel
) {

    public static MemberResponse fromMemberDto(MemberDto memberDto) {
        return new MemberResponse(
                memberDto.id(),
                memberDto.email(),
                memberDto.password(),
                memberDto.username(),
                memberDto.nickname(),
                memberDto.tel()
        );
    }
}
