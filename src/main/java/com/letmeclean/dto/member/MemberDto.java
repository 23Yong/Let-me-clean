package com.letmeclean.dto.member;

import com.letmeclean.model.member.Member;

import java.io.Serializable;
import java.time.LocalDateTime;

public record MemberDto(
        Long id,
        String email,
        String password,
        String username,
        String nickname,
        String tel
) implements Serializable {

    public static MemberDto of(String email, String password, String username, String nickname, String tel) {
        return new MemberDto(
                null,
                email,
                password,
                username,
                nickname,
                tel
        );
    }

    public static MemberDto of(Long id, String email, String password, String username, String nickname, String tel) {
        return new MemberDto(
                id,
                email,
                password,
                username,
                nickname,
                tel
        );
    }

    public Member toEntity(String password) {
        return Member.of(
                email,
                password,
                username,
                nickname,
                tel
        );
    }

    public Member toEntity() {
        return Member.of(
                email,
                password,
                username,
                nickname,
                tel
        );
    }

    public static MemberDto fromEntity(Member entity) {
        return new MemberDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUsername(),
                entity.getNickname(),
                entity.getTel()
        );
    }
}
