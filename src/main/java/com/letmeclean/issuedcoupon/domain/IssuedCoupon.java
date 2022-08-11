package com.letmeclean.issuedcoupon.domain;

import com.letmeclean.coupon.domain.Coupon;
import com.letmeclean.coupon.domain.CouponStatus;
import com.letmeclean.global.BaseTimeEntity;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IssuedCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "issue_coupon_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private IssuedCouponStatus issuedCouponStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void useCoupon() {
        if (this.coupon.getCouponStatus() == CouponStatus.EXPIRED) {
            ErrorCode.throwBadRequestCouponExpiredTime();
        }
        this.issuedCouponStatus = IssuedCouponStatus.USED;
        member.getIssuedCoupons().remove(this);
        member.addPoint(coupon.getPointAmount());
    }

    public void linkMember(Member member) {
        this.member = member;
    }

    @Builder
    public IssuedCoupon(IssuedCouponStatus issuedCouponStatus, Coupon coupon, Member member) {
        this.issuedCouponStatus = issuedCouponStatus;
        this.coupon = coupon;
        this.member = member;
    }
}
