package com.letmeclean.common.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public static String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.split(" ")[1].trim();
        }
        return null;
    }
}
