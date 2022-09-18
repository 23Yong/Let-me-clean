package com.letmeclean.dto.payment.api.dto;

import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;

public record PaymentApproveDto(
        String email,
        String tid,
        String movieTitle,
        Integer quantity,
        Integer totalAmount
) {

    private PaymentApproveDto(String email, KakaoPayApproveResponse kakaoPayApproveResponse) {
        this(
                email,
                kakaoPayApproveResponse.getTid(),
                kakaoPayApproveResponse.getItemName(),
                kakaoPayApproveResponse.getQuantity(),
                kakaoPayApproveResponse.getTotalAmount()
        );
    }

    public static PaymentApproveDto of(String email, KakaoPayApproveResponse kakaoPayApproveResponse) {
        return new PaymentApproveDto(email, kakaoPayApproveResponse);
    }
}
