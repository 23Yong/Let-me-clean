package com.letmeclean.payment.dto.response;

import com.letmeclean.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
