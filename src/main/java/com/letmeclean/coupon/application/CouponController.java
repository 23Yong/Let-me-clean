package com.letmeclean.coupon.application;

import com.letmeclean.coupon.dto.request.CouponRequest.CouponRegisterRequestDto;
import com.letmeclean.coupon.service.CouponService;
import com.letmeclean.global.constants.ResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<Void> registerCoupon(@RequestBody CouponRegisterRequestDto couponRegisterRequestDto) {
        couponService.registerCoupon(couponRegisterRequestDto);

        return ResponseConstants.CREATED;
    }
}
