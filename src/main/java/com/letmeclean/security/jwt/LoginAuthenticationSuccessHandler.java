package com.letmeclean.security.jwt;

import com.letmeclean.common.constants.RedisJwtConstants;
import com.letmeclean.common.utils.SecurityUtil;
import com.letmeclean.controller.dto.TokenDto;
import com.letmeclean.common.redis.refreshtoken.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN_HEADER = "Refresh_token";
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenDto.TokenInfo token = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(String.valueOf(SecurityUtil.getCurrentMemberId()))
                .value(token.getRefreshToken())
                .expirationTime(token.getRefreshTokenExpirationTime())
                .build();

        redisTemplate.opsForValue()
                        .set(
                                RedisJwtConstants.JWT_PREFIX + SecurityUtil.getCurrentMemberId(),
                                refreshToken.getValue(),
                                refreshToken.getExpirationTime(),
                                TimeUnit.MILLISECONDS
                        );

        response.setHeader(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, token.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
