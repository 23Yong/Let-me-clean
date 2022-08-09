package com.letmeclean.ticket.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.issuedticket.domain.IssuedTicket;
import com.letmeclean.issuedticket.domain.IssuedTicketStatus;
import com.letmeclean.issuedticket.service.TicketIssueService;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.service.MemberService;
import com.letmeclean.payment.domain.Payment;
import com.letmeclean.payment.domain.PaymentStatus;
import com.letmeclean.payment.service.PaymentService;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import com.letmeclean.ticket.dto.request.TicketRequest;
import com.letmeclean.ticket.dto.request.TicketRequest.TicketSoldRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TicketService {

    private final TicketIssueService ticketIssueService;
    private final MemberService memberService;
    private final PaymentService paymentService;

    private final TicketRepository ticketRepository;

    public Ticket findTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> ErrorCode.throwTicketNotFound());
    }

    @Transactional
    public void register(TicketRequest.TicketSaveRequestDto ticketSaveRequestDto) {
        if (ticketRepository.existsByName(ticketSaveRequestDto.getName())) {
            ErrorCode.throwDuplicateTicketConflict();
        }
        Ticket ticket = ticketSaveRequestDto.toEntity();

        ticketRepository.save(ticket);
    }

    @Transactional
    public void sold(TicketSoldRequestDto ticketSoldRequest) {
        Member member = memberService.findMember(ticketSoldRequest.getEmail());
        Ticket ticket = findTicket(ticketSoldRequest.getTicketId());

        List<IssuedTicket> issuedTickets = new ArrayList<>();
        for (int i = 0; i < ticketSoldRequest.getQuantity(); i++) {
            IssuedTicket issuedTicket = IssuedTicket.builder()
                    .issuedTicketStatus(IssuedTicketStatus.TICKET_NOT_USED)
                    .member(member)
                    .ticket(ticket)
                    .build();
            issuedTickets.add(issuedTicket);
            member.addIssuedTicket(issuedTicket);
        }

        ticketIssueService.issueTickets(issuedTickets);

        Payment payment = new Payment(
                PaymentStatus.TICKET_PAY_COMPLETED,
                ticketSoldRequest.getTotalPrice(),
                ticketSoldRequest.getQuantity(),
                member,
                ticket
        );
        member.addPayment(payment);
        paymentService.savePayment(payment);
    }
}
