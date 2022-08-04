package com.letmeclean.issuedticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedTicketRepository extends JpaRepository<IssuedTicket, Long> {
}
