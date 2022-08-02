package com.letmeclean.ticket.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import com.letmeclean.ticket.dto.request.TicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public void register(TicketRequest.TicketSaveRequestDto ticketSaveRequestDto) {
        if (ticketRepository.existsByName(ticketSaveRequestDto.getName())) {
            ErrorCode.throwDuplicateTicketConflict();
        }
        Ticket ticket = ticketSaveRequestDto.toEntity();

        ticketRepository.save(ticket);
    }
}
