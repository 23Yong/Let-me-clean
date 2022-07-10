package com.letmeclean.controller.dto.member;

import com.letmeclean.domain.member.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberRequest {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDto {

        private String email;
        private String password;
        private String username;
        private String nickname;
        private String tel;

        @Builder
        public SignUpRequestDto(String email, String password, String username, String nickname, String tel) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.nickname = nickname;
            this.tel = tel;
        }

        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .name(username)
                    .nickname(nickname)
                    .tel(tel)
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginRequestDto {

        @Email
        private String email;
        @NotBlank
        private String password;

        @Builder
        public LoginRequestDto(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LogoutRequestDto {

        private String accessToken;

        @Builder
        public LogoutRequestDto(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
