package com.letmeclean.service;

import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketStatus;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.payment.Payment;
import com.letmeclean.model.payment.PaymentStatus;
import com.letmeclean.service.payment.service.PaymentService;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import com.letmeclean.dto.ticket.request.TicketRequest;
import com.letmeclean.dto.ticket.request.TicketRequest.TicketSoldRequestDto;
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
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.TICKET_NOT_FOUND, String.format("%s 을(를) 찾을 수 없습니다.", ticketId)));
    }

    @Transactional
    public void register(TicketDto ticketDto) {
        if (ticketRepository.existsByName(ticketDto.name())) {
            throw new LetMeCleanException(ErrorCode.DUPLICATE_TICKET_CONFLICT, String.format("%s 은(는) 이미 존재하는 이름입니다.", ticketDto.name()));
        }

        ticketRepository.save(ticketDto.toEntity());
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
