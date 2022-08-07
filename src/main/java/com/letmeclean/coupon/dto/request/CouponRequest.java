package com.letmeclean.coupon.dto.request;

import com.letmeclean.coupon.domain.Coupon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CouponRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class CouponRegisterRequestDto {

        @NotBlank
        private String name;

        @NotNull
        private Integer pointAmount;

        @NotNull
        private LocalDateTime expiredAt;

        public Coupon toEntity() {
            return new Coupon(name, pointAmount, expiredAt);
        }
    }
}
