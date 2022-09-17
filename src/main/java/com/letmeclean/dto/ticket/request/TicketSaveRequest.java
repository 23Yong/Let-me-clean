package com.letmeclean.dto.ticket.request;

import com.letmeclean.dto.ticket.TicketDto;

import javax.validation.constraints.Min;
import java.io.Serializable;

public record TicketSaveRequest(
        String name,
        @Min(0) Integer price,
        String description
) {

    public static TicketSaveRequest of(String name, @Min(0) Integer price, String description) {
        return new TicketSaveRequest(name, price, description);
    }

    public TicketDto toDto() {
        return TicketDto.of( name, price, description);
    }
}
