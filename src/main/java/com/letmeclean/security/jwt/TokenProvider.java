package com.letmeclean.security.jwt;

import com.letmeclean.controller.dto.TokenDto.TokenInfo;
import com.letmeclean.exception.auth.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.security.Keys;

import static java.time.Instant.*;
import static java.util.stream.Collectors.*;

@Component
public class TokenProvider {

    private static final String AUTHORITY_KEY = "auth";

    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    private final Key key;
    private final JwtParser parser;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-expire-time}") String accessTokenExpireTime,
                         @Value("${jwt.refresh-token-expire-time}") String refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        parser = Jwts.parserBuilder().setSigningKey(key).build();
        this.accessTokenExpireTime = Long.valueOf(accessTokenExpireTime);
        this.refreshTokenExpireTime = Long.valueOf(refreshTokenExpireTime);
    }

    public TokenInfo createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(","));

        long now = (new Date()).getTime();

        // Access Token
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITY_KEY, authorities)
                .setExpiration(Date.from(now().plusMillis(accessTokenExpireTime)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = parser.parseClaimsJws(jwt).getBody();

        if (claims.get(AUTHORITY_KEY) == null) {
            throw InvalidTokenException.getInstance();
        }

        List<? extends GrantedAuthority> authorities = Arrays.stream(
                    claims.get(AUTHORITY_KEY, String.class).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    public boolean validateToken(String jwt) {
        try {
            parser.parseClaimsJws(jwt);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 이 잘못되었습니다.");
        }
    }
}
