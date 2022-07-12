package com.letmeclean.service;

import com.letmeclean.domain.refreshtoken.RefreshToken;
import com.letmeclean.domain.refreshtoken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}
