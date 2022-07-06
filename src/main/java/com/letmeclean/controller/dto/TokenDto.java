package com.letmeclean.controller.dto;

import lombok.*;

public class TokenDto {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenInfo {

        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiredTime;

        @Builder
        public TokenInfo(String grantType, String accessToken, String refreshToken, Long accessTokenExpiredTime) {
            this.grantType = grantType;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.accessTokenExpiredTime = accessTokenExpiredTime;
        }
    }
}
