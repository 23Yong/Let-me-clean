package com.letmeclean.global.security.jwt;

import com.letmeclean.global.redis.refreshtoken.RedisRefreshTokenRepository;
import com.letmeclean.global.redis.refreshtoken.RefreshToken;
import com.letmeclean.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.letmeclean.global.dto.TokenDto.*;

@RequiredArgsConstructor
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenInfo tokenInfo = JwtUtil.resolveToken(request);
        Authentication providerAuthentication = tokenProvider.getAuthentication(tokenInfo.getAccessToken());
        String email = providerAuthentication.getName();

        RefreshToken refreshToken = redisRefreshTokenRepository.findByEmail(email).orElseThrow();
        redisRefreshTokenRepository.delete(refreshToken);
    }
}
