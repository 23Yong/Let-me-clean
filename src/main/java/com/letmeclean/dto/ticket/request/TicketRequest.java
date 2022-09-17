package com.letmeclean.dto.ticket.request;

import lombok.*;

public class TicketRequest {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class TicketSoldRequestDto {

        private String email;
        private Integer quantity;
        private Integer totalPrice;
        private Long ticketId;
    }
}
