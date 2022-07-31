package com.letmeclean.payment.application.dto;

import lombok.Getter;

@Getter
public class PaymentReadyRequest {

    private Long ticketId;
    private Integer ticketQuantity;
}
