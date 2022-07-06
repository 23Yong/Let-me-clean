package com.letmeclean.service;

import com.letmeclean.controller.dto.TokenDto.TokenInfo;
import com.letmeclean.domain.refreshtoken.RefreshToken;
import com.letmeclean.domain.refreshtoken.RefreshTokenRepository;
import com.letmeclean.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.letmeclean.controller.dto.member.MemberRequest.*;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenInfo login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfo token = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(token.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public TokenInfo reissue(TokenInfo tokenDto) {
        if (!tokenProvider.validateToken(tokenDto.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 정보가 유저 정보와 일치하지 않습니다.");
        }

        TokenInfo newToken = tokenProvider.createToken(authentication);

        refreshToken.updateValue(newToken.getRefreshToken());

        return newToken;
    }
}
