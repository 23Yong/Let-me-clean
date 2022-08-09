package com.letmeclean.issuedcoupon.application;

import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.global.constants.ResponseConstants;
import com.letmeclean.issuedcoupon.domain.IssuedCoupon;
import com.letmeclean.issuedcoupon.dto.request.IssuedCouponRequest;
import com.letmeclean.issuedcoupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.letmeclean.issuedcoupon.dto.request.IssuedCouponRequest.*;

@RequiredArgsConstructor
@RestController
public class IssuedCouponController {

    private final CouponIssueService couponIssueService;

    @PostMapping("/issue-coupon/{couponId}")
    public ResponseEntity<Void> issueCouponToMember(@CurrentEmail String email, @PathVariable Long couponId) {
        IssuedCouponRequestDto issuedCouponRequestDto = new IssuedCouponRequestDto(email, couponId);
        couponIssueService.issueCouponToMember(issuedCouponRequestDto);

        return ResponseConstants.OK;
    }
}
