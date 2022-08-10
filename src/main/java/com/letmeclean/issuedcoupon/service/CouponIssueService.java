package com.letmeclean.issuedcoupon.service;

import com.letmeclean.coupon.domain.Coupon;
import com.letmeclean.coupon.service.CouponService;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.issuedcoupon.domain.IssuedCoupon;
import com.letmeclean.issuedcoupon.domain.IssuedCouponRepository;
import com.letmeclean.issuedcoupon.domain.IssuedCouponStatus;
import com.letmeclean.issuedcoupon.dto.request.IssuedCouponRequest.IssuedCouponExchangeRequestDto;
import com.letmeclean.issuedcoupon.dto.request.IssuedCouponRequest.IssuedCouponRequestDto;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponIssueService {

    private final IssuedCouponRepository issuedCouponRepository;

    private final MemberService memberService;
    private final CouponService couponService;

    public IssuedCoupon findIssuedCoupon(Long issuedCouponId) {
        return issuedCouponRepository.findById(issuedCouponId)
                .orElseThrow(() -> ErrorCode.throwIssuedCouponNotFound());
    }

    @Transactional
    public void issueCouponToMember(IssuedCouponRequestDto issuedCouponRequestDto) {
        Coupon coupon = couponService.findCoupon(issuedCouponRequestDto.getCouponId());
        Member member = memberService.findMember(issuedCouponRequestDto.getEmail());

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .issuedCouponStatus(IssuedCouponStatus.NOT_USED)
                .coupon(coupon)
                .member(member)
                .build();
        member.addIssuedCoupon(issuedCoupon);

        issuedCouponRepository.save(issuedCoupon);
    }

    @Transactional
    public void exchangeCouponToPoint(IssuedCouponExchangeRequestDto issuedCouponExchangeRequestDto) {
        IssuedCoupon issuedCoupon = findIssuedCoupon(issuedCouponExchangeRequestDto.getIssuedCouponId());
        issuedCoupon.useCoupon();
    }
}
