package com.letmeclean.dto.payment.dto;

import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentApproveDto {

    private final String email;
    private final String tid;
    private final String movieTitle;
    private final Integer quantity;
    private final Integer totalAmount;

    public PaymentApproveDto(String email, KakaoPayApproveResponse kakaoPayApproveResponse) {
        this(
                email,
                kakaoPayApproveResponse.getTid(),
                kakaoPayApproveResponse.getItemName(),
                kakaoPayApproveResponse.getQuantity(),
                kakaoPayApproveResponse.getTotalAmount()
        );
    }
}