package com.letmeclean.model.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PAY_READY,
    TICKET_PAY_COMPLETED,
    TICKET_PAY_FAILED
}
