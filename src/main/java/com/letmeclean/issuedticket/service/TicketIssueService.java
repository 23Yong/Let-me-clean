package com.letmeclean.issuedticket.service;

import com.letmeclean.issuedticket.domain.IssuedTicket;
import com.letmeclean.issuedticket.domain.IssuedTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketIssueService {

    private final IssuedTicketRepository issuedTicketRepository;

    @Transactional
    public void issueTickets(List<IssuedTicket> issuedTickets) {
        issuedTicketRepository.saveAll(issuedTickets);
    }
}
