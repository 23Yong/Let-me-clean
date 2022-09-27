package com.letmeclean.dto.payment.response;

import com.letmeclean.model.payment.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        String email,
        Integer totalPrice,
        Integer quantity,
        String ticketName,
        PaymentStatus paymentStatus,
        LocalDateTime paidAt
) {

}
