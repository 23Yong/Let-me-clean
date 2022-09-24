package com.letmeclean.fixture;

import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketStatus;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.ticket.Ticket;

public class IssuedTicketEntityFixture {

    public static IssuedTicket get(Member member, Ticket ticket) {
        return IssuedTicket.of(
                IssuedTicketStatus.TICKET_NOT_USED,
                member,
                ticket
        );
    }
}
