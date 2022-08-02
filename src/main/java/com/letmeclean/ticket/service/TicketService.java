package com.letmeclean.ticket.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.issuedticket.domain.IssuedTicket;
import com.letmeclean.issuedticket.domain.IssuedTicketRepository;
import com.letmeclean.issuedticket.domain.IssuedTicketStatus;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.domain.MemberRepository;
import com.letmeclean.payment.domain.Payment;
import com.letmeclean.payment.domain.PaymentRepository;
import com.letmeclean.payment.domain.PaymentStatus;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import com.letmeclean.ticket.dto.request.TicketRequest;
import com.letmeclean.ticket.dto.request.TicketRequest.TicketSoldRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final IssuedTicketRepository issuedTicketRepository;

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
        String email = ticketSoldRequest.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> ErrorCode.throwMemberNotFound());

        Ticket ticket = ticketRepository.findById(ticketSoldRequest.getTicketId())
                        .orElseThrow(() -> ErrorCode.throwTicketNotFound());

        IssuedTicket issuedTicket = IssuedTicket.builder()
                .issuedTicketStatus(IssuedTicketStatus.TICKET_NOT_USED)
                .member(member)
                .ticket(ticket)
                .build();
        issuedTicketRepository.save(issuedTicket);

        Payment payment = new Payment(
                PaymentStatus.TICKET_PAY_COMPLETED,
                ticketSoldRequest.getTotalPrice(),
                member,
                ticket
        );
        member.addPayment(payment);

        paymentRepository.save(payment);
    }
}
