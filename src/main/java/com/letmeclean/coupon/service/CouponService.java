package com.letmeclean.coupon.service;

import com.letmeclean.coupon.domain.Coupon;
import com.letmeclean.coupon.domain.CouponRepository;
import com.letmeclean.coupon.dto.request.CouponRequest.CouponRegisterRequestDto;
import com.letmeclean.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public void registerCoupon(CouponRegisterRequestDto couponRegisterRequest) {
        Coupon coupon = couponRegisterRequest.toEntity();

        if (coupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            ErrorCode.throwBadRequestCouponExpiredTime();
        }
        couponRepository.save(coupon);
    }

    public Coupon findCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> ErrorCode.throwCouponNotFound());
    }
}
