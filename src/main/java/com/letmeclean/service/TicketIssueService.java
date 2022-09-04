package com.letmeclean.service;

import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
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
