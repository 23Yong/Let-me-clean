package com.letmeclean.coupon.domain;

import com.letmeclean.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    @NotBlank
    private String name;

    @Min(0)
    private Integer pointAmount;

    @NotNull
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Builder
    public Coupon(String name, Integer pointAmount, LocalDateTime expiredAt) {
        this.name = name;
        this.pointAmount = pointAmount;
        this.expiredAt = expiredAt;
        this.couponStatus = CouponStatus.POSSIBLE;
    }
}
