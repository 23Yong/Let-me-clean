package com.letmeclean.global.security.jwt.dto;

import lombok.*;

public class TokenDto {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenInfo {

        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;

        @Builder
        public TokenInfo(String accessToken, String refreshToken, Long refreshTokenExpirationTime) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        }
    }
}
