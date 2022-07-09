package com.letmeclean.security.jwt;

import com.letmeclean.common.utils.SecurityUtil;
import com.letmeclean.controller.dto.TokenDto;
import com.letmeclean.domain.refreshtoken.RefreshToken;
import com.letmeclean.domain.refreshtoken.RefreshTokenRepository;
import com.letmeclean.service.AuthService;
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
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenDto.TokenInfo token = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(SecurityUtil.getCurrentMemberId())
                .value(token.getRefreshToken())
                .build();

        authService.saveRefreshToken(refreshToken);

        response.setHeader(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, token.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
