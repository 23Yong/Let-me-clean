package com.letmeclean.controller.dto.cleaner;

import com.letmeclean.domain.cleaner.Cleaner;
import lombok.*;

public class CleanerRequest {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDto {

        private String email;
        private String password;
        private String username;
        private String tel;

        @Builder
        public SignUpRequestDto(String email, String password, String username, String tel) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.tel = tel;
        }

        public Cleaner toEntity() {
            return Cleaner.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .tel(tel)
                    .build();
        }
    }
}
