package com.letmeclean.dto.payment.request;

import lombok.Getter;

@Getter
public class PaymentReadyRequest {

    private Long ticketId;
    private Integer ticketQuantity;
}
