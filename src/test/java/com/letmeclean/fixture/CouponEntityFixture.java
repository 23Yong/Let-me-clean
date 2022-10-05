package com.letmeclean.fixture;

import com.letmeclean.model.coupon.Coupon;
import com.letmeclean.model.coupon.DiscountType;

import java.time.LocalDateTime;

public class CouponEntityFixture {

    public static Coupon get() {
        return Coupon.of(
                "회원가입 할인 쿠폰",
                DiscountType.PERCENTAGE,
                50,
                LocalDateTime.now().plusDays(14)
        );
    }
}
