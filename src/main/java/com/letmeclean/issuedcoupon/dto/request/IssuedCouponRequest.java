package com.letmeclean.issuedcoupon.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class IssuedCouponRequest {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class IssuedCouponRequestDto {

        @NotBlank
        private String email;
        @NotNull
        private Long couponId;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class IssuedCouponExchangeRequestDto {

        @NotBlank
        private String email;
        @NotNull
        private Long issuedCouponId;
    }
}
