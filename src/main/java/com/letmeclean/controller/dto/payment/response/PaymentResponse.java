package com.letmeclean.controller.dto.payment.response;

import com.letmeclean.domain.payment.Payment;
import com.letmeclean.domain.payment.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentResponse {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PaymentDetailResponseDto {

        private Integer totalPrice;
        private PaymentStatus paymentStatus;
        private LocalDateTime paidAt;

        public static PaymentDetailResponseDto toResponse(Payment payment) {
            return new PaymentDetailResponseDto(
                    payment.getTotalPrice(),
                    payment.getPaymentStatus(),
                    payment.getCreatedAt()
            );
        }
    }
}
