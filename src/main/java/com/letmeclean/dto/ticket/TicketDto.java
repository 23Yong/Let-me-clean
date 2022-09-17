package com.letmeclean.dto.ticket;

import com.letmeclean.model.ticket.Ticket;

import javax.validation.constraints.Min;
import java.io.Serializable;

public record TicketDto(
        Long id,
        String name,
        @Min(0) Integer price,
        String description
) {

    public static TicketDto of(Long id, String name, @Min(0) Integer price, String description) {
        return new TicketDto(id, name, price, description);
    }

    public static TicketDto of(String name, @Min(0) Integer price, String description) {
        return new TicketDto(null, name, price, description);
    }

    public Ticket toEntity() {
        return Ticket.of(name, price, description);
    }
}
