package com.letmeclean.controller.dto;

import lombok.*;

public class TokenDto {

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenInfo {

        private String accessToken;
        private String refreshToken;

        @Builder
        public TokenInfo(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
