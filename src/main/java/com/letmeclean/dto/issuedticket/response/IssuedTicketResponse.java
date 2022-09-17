package com.letmeclean.dto.issuedticket.response;

import java.time.LocalDateTime;

public record IssuedTicketResponse(
        String email,
        String ticketName,
        String ticketDescription,
        LocalDateTime issuedAt
) {

}
