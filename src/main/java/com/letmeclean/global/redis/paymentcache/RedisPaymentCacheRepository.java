package com.letmeclean.global.redis.paymentcache;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisPaymentCacheRepository extends CrudRepository<PaymentCache, Long> {

    Optional<PaymentCache> findByEmail(String email);
}
