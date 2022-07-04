package com.letmeclean.controller.dto.response.member;

import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpResponseDto {

        private String email;
        private String username;
        private String nickname;
        private String tel;
        private MemberStatus status;

        @Builder
        public SignUpResponseDto(String email, String username, String nickname, String tel, MemberStatus status) {
            this.email = email;
            this.username = username;
            this.nickname = nickname;
            this.tel = tel;
            this.status = status;
        }

        public SignUpResponseDto(Member member) {
            this.email = member.getEmail();
            this.username = member.getUsername();
            this.nickname = member.getNickname();
            this.tel = member.getTel();
            this.status = MemberStatus.DEFAULT;
        }
    }
}
