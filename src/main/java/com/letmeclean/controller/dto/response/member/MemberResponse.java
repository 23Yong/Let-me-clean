package com.letmeclean.controller.dto.response.member;

import com.letmeclean.domain.member.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Getter
    @NoArgsConstructor
    public static class SignUpResponseDto {

        private String email;
        private String name;
        private String nickname;
        private String tel;
        private MemberStatus status;

        @Builder
        public SignUpResponseDto(String email, String name, String nickname, String tel, MemberStatus status) {
            this.email = email;
            this.name = name;
            this.nickname = nickname;
            this.tel = tel;
            this.status = status;
        }
    }
}
