package com.letmeclean.model.coupon;

import com.letmeclean.model.AuditingFields;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String couponName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DiscountType discountType;

    @Column(nullable = false)
    private Integer discountPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private Coupon(String couponName, DiscountType discountType, Integer discountPrice, LocalDateTime expiredAt) {
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountPrice = discountPrice;
        this.expiredAt = expiredAt;
    }

    public static Coupon of(String couponName, DiscountType discountType, Integer discountPrice, LocalDateTime expiredAt) {
        return new Coupon(couponName, discountType, discountPrice, expiredAt);
    }
}
