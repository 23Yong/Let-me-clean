package com.letmeclean.fixture;

import com.letmeclean.model.ticket.Ticket;

public class TicketEntityFixture {

    public static Ticket get() {
        return Ticket.of(
                "BASIC_TICKET",
                10000,
                "수거함을 수거해가는 기본티켓입니다."
        );
    }
}
