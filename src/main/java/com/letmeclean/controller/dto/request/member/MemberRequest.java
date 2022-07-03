package com.letmeclean.controller.dto.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberRequest {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SignUpRequestDto {

        private String email;
        private String password;
        private String confirmPassword;
        private String name;
        private String nickname;
        private String tel;

        @Builder
        public SignUpRequestDto(String email, String password, String confirmPassword, String name, String nickname, String tel) {
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.name = name;
            this.nickname = nickname;
            this.tel = tel;
        }
    }
}
