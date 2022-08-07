package com.letmeclean.member.dto;

import com.letmeclean.member.domain.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberRequest {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDto {

        @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EditInfoRequestDto {

        @NotBlank
        private String nickname;
        @NotBlank
        private String tel;

        public EditInfoRequestDto(String nickname, String tel) {
            this.nickname = nickname;
            this.tel = tel;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EditPasswordRequestDto {

        private String prevPassword;
        @NotBlank
        private String newPassword;

        public EditPasswordRequestDto(String prevPassword, String newPassword) {
            this.prevPassword = prevPassword;
            this.newPassword = newPassword;
        }
    }
}
