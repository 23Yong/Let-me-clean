package com.letmeclean.global.redis.refreshtoken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByEmail(String email);
}
