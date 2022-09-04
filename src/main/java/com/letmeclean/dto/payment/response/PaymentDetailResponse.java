package com.letmeclean.dto.payment.response;

import com.letmeclean.model.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentDetailResponse {

    private String email;
    private Integer totalPrice;
    private Integer quantity;
    private String ticketName;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
}
