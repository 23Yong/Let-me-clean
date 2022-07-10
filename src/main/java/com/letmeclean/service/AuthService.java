package com.letmeclean.service;

import com.letmeclean.common.utils.SecurityUtil;
import com.letmeclean.controller.dto.TokenDto.TokenInfo;
import com.letmeclean.domain.refreshtoken.RefreshToken;
import com.letmeclean.domain.refreshtoken.RefreshTokenRepository;
import com.letmeclean.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Long currentMemberId = Long.valueOf(authentication.getName());

        if (refreshTokenRepository.existsById(currentMemberId)) {
            refreshTokenRepository.deleteById(currentMemberId);
        }
    }
}
