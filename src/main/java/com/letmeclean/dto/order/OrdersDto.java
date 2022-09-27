package com.letmeclean.dto.order;

import com.letmeclean.model.order.OrderState;

public record OrdersDto(
        Long id,
        OrderState orderState,
        String address,
        String additionalInfo,
        String email,
        Long issuedTicketId
) {

    public static OrdersDto of(Long id, OrderState orderState, String address, String additionalInfo, String email, Long issuedTicketId) {
        return new OrdersDto(
                null,
                OrderState.REQUESTED,
                address,
                additionalInfo,
                email,
                issuedTicketId
        );
    }
}
