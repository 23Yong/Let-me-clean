package com.letmeclean.dto.payment.request;

public record PaymentReadyRequest(
        Long ticketId,
        Integer ticketQuantity
) {

}
