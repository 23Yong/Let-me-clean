package com.letmeclean.dto.issuedticket;

import com.letmeclean.dto.member.MemberDto;
import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.model.issuedticket.IssuedTicketStatus;

public record IssuedTicketDto(
        Long id,
        IssuedTicketStatus issuedTicketStatus,
        MemberDto member,
        TicketDto ticket
) {
}
