package com.letmeclean.security.jwt;

import com.letmeclean.common.redis.refreshtoken.RedisRefreshTokenRepository;
import com.letmeclean.common.utils.SecurityUtil;
import com.letmeclean.controller.dto.TokenDto;
import com.letmeclean.common.redis.refreshtoken.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN_HEADER = "Refresh_token";
    private final TokenProvider tokenProvider;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenDto.TokenInfo token = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(SecurityUtil.getCurrentMemberEmail())
                .value(token.getRefreshToken())
                .build();

        redisRefreshTokenRepository.save(refreshToken);

        response.setHeader(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, token.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
