package com.letmeclean.global.utils;

import com.letmeclean.global.dto.TokenDto.TokenInfo;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "Refresh_token";
    private static final String BEARER_PREFIX = "Bearer ";

    public static TokenInfo resolveToken(HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN_HEADER);

        String accessToken = null;
        String refreshToken = null;
        if (StringUtils.hasText(accessTokenHeader) && accessTokenHeader.startsWith(BEARER_PREFIX)) {
            accessToken = accessTokenHeader.split(" ")[1].trim();
        }
        if (StringUtils.hasText(refreshTokenHeader)) {
            refreshToken = refreshTokenHeader;
        }

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
