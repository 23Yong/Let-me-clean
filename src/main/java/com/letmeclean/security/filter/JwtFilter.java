package com.letmeclean.security.filter;

import com.letmeclean.common.utils.JwtUtil;
import com.letmeclean.controller.dto.TokenDto.TokenInfo;
import com.letmeclean.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TokenInfo tokenInfo = JwtUtil.resolveToken(request);

        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        if (accessToken != null && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
            TokenInfo reissueTokenInfo = reissue(accessToken);
            response.setHeader(HttpHeaders.AUTHORIZATION, reissueTokenInfo.getAccessToken());
            response.setStatus(HttpServletResponse.SC_CREATED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private TokenInfo reissue(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        TokenInfo reissueTokenInfo = tokenProvider.createToken(authentication);
        return reissueTokenInfo;
    }
}
