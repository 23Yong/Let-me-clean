package com.letmeclean.model.isseudcoupon;

import com.letmeclean.model.AuditingFields;
import com.letmeclean.model.coupon.Coupon;
import com.letmeclean.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IssuedCoupon extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private IssuedCouponStatus issuedCouponStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public void useCoupon() {
        this.issuedCouponStatus = IssuedCouponStatus.USED;
    }

    private IssuedCoupon(IssuedCouponStatus issuedCouponStatus, Member member, Coupon coupon) {
        this.issuedCouponStatus = issuedCouponStatus;
        this.member = member;
        this.coupon = coupon;
    }

    public static IssuedCoupon of(IssuedCouponStatus issuedCouponStatus, Member member, Coupon coupon) {
        return new IssuedCoupon(issuedCouponStatus, member, coupon);
    }
}
