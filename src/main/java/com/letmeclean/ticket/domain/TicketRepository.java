package com.letmeclean.ticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByName(String name);
}
