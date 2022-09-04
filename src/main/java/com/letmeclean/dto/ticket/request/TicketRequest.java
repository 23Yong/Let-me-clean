package com.letmeclean.dto.ticket.request;

import com.letmeclean.model.ticket.Ticket;
import lombok.*;

public class TicketRequest {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class TicketSaveRequestDto {

        private String name;
        private Integer price;
        private String description;

        public Ticket toEntity() {
            return Ticket.builder()
                    .name(name)
                    .price(price)
                    .description(description)
                    .build();
        }
    }

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