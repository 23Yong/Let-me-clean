package com.letmeclean.global.redis.paymentcache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("Payment")
public class PaymentCache {

    @Id
    @GeneratedValue
    @Column(name = "payment_ready_id")
    private Long id;

    @Indexed
    private String email;

    private String ticketName;

    private String tid;

    private Long ticketId;

    private Long issuedCouponId;

    private String paymentNumber;

    private PaymentCache(String email, String ticketName, String tid, Long ticketId, Long issuedCouponId, String paymentNumber) {
        this.email = email;
        this.ticketName = ticketName;
        this.tid = tid;
        this.ticketId = ticketId;
        this.issuedCouponId = issuedCouponId;
        this.paymentNumber = paymentNumber;
    }

    public static PaymentCache of(String email, String ticketName, String tid, Long ticketId, Long issuedCouponId, String paymentNumber) {
        return new PaymentCache(email, ticketName, tid, ticketId, issuedCouponId, paymentNumber);
    }
}
