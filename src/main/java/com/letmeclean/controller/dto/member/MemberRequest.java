package com.letmeclean.controller.dto.member;

import com.letmeclean.domain.member.Member;
import com.letmeclean.security.roles.Role;
import lombok.*;

public class MemberRequest {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDto {

        private String email;
        private String password;
        private String confirmPassword;
        private String username;
        private String nickname;
        private String tel;

        public void setPassword(String hashedPassword) {
            this.password = hashedPassword;
        }

        @Builder
        public SignUpRequestDto(String email, String password, String confirmPassword, String username, String nickname, String tel) {
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
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
                    .role(Role.ROLE_MEMBER)
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginRequestDto {

        private String email;
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
