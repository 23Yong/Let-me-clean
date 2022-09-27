package com.letmeclean.dto.order.request;

import com.letmeclean.dto.order.OrdersDto;
import com.letmeclean.model.order.OrderState;

public record OrdersRequest(
        String address,
        String additionalInfo,
        Long issuedTicketId
) {

    public OrdersDto toDto(String email) {
        return OrdersDto.of(
                null,
                OrderState.REQUESTED,
                address,
                additionalInfo,
                email,
                issuedTicketId
        );
    }
}
