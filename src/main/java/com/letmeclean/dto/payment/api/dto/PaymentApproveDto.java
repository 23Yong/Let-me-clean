package com.letmeclean.dto.payment.api.dto;

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
