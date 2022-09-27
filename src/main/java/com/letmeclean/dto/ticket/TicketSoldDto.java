package com.letmeclean.dto.ticket;

public record TicketSoldDto(
        String email,
        Integer quantity,
        Integer totalPrice,
        Long ticketId
) {

    public static TicketSoldDto of(String email, Integer quantity, Integer totalPrice, Long ticketId) {
        return new TicketSoldDto(email, quantity, totalPrice, ticketId);
    }
}
