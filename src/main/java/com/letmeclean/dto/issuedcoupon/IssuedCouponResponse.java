package com.letmeclean.dto.issuedcoupon;

import com.letmeclean.model.isseudcoupon.IssuedCouponStatus;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.letmeclean.model.isseudcoupon.IssuedCoupon} entity
 */
public record IssuedCouponResponse(
        IssuedCouponStatus issuedCouponStatus,
        Integer discountPrice,
        LocalDateTime expiredAt
) {

    public IssuedCouponResponse(IssuedCouponStatus issuedCouponStatus, Integer discountPrice, LocalDateTime expiredAt) {
        this.issuedCouponStatus = issuedCouponStatus;
        this.discountPrice = discountPrice;
        this.expiredAt = expiredAt;
    }
}